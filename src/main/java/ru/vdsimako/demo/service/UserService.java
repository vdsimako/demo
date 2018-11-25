package ru.vdsimako.demo.service;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.vdsimako.demo.model.Request;
import ru.vdsimako.demo.model.User;
import ru.vdsimako.demo.model.enums.RequestStatus;
import ru.vdsimako.demo.model.enums.UserStatus;
import ru.vdsimako.demo.repository.RequestRepository;
import ru.vdsimako.demo.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;
    private RequestRepository requestRepository;

    public UserService(UserRepository repository,
                       RequestRepository requestRepository) {
        this.userRepository = repository;
        this.requestRepository = requestRepository;
    }

    public User logOutUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());

        log.info("find user" + optionalUser.get().toString());

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            user.setUserStatus(UserStatus.OFFLINE);
            user = userRepository.save(user);
        }

        return user;
    }

    @Async
    public void spreadRequestsBetweenActiveUsers(List<Request> requestList) {

        requestList.removeIf(request -> !request.getRequestStatus().equals(RequestStatus.OPEN));

        Queue<User> userQueue = new PriorityQueue<>(Comparator.comparingInt(o -> (null != o.getRequestList() ? o.getRequestList().size() : 0)));

        List<User> allByUserStatus = userRepository.findAllByUserStatus(UserStatus.ONLINE);

        if (!allByUserStatus.isEmpty()) {

            userQueue.addAll(allByUserStatus);

            for (Request request : requestList) {
                User user = userQueue.poll();
                if (null != user.getRequestList()) {
                    user.getRequestList().add(request);
                } else {
                    user.setRequestList(Collections.singletonList(request));
                }
                userQueue.add(user);
            }

            userRepository.saveAll(allByUserStatus);

        } else {
            requestList.forEach(request -> request.setResponsibleUser(null));

            requestRepository.saveAll(requestList);
        }
    }

    @Async
    public void spreadRequests(List<Request> requestList) {
        List<User> allActiveUsers = userRepository.findAllByUserStatus(UserStatus.ONLINE);

        int N = allActiveUsers.size();

        long l = requestRepository.countAllByRequestStatus(RequestStatus.OPEN);

//        Целая часть от среднего количества задач от пользователя
//        Сумма задач деленная на количетсво задача в статусе открыто
        long alfa = l/N;


//        Откинуть тех пользователей которых задач больше среднего и отсортировать список по возрастанию задач
//        Чтобы первые в списке были те пользователи у коотрых минимальное количество задач
        List<User> collect = allActiveUsers.stream().filter(user -> {

            if (null == user.getRequestList()) {
                user.setRequestList(new LinkedList<>());
            }

            return user.getRequestList().size() >= alfa;
        })
                .sorted(Comparator.comparingInt(o -> o.getRequestList().size()))
                .collect(Collectors.toList());

//        Целая часть от среднего
//        количество задач до которого надо поднять всем в списке collect
        long i = l/collect.size();
//        Дробная часть от среднего
//        то количество задач которое надо до раскидать по пользователям
        long j = l%collect.size();

        int k = 0;

        for (User user : allActiveUsers) {
           user.getRequestList().addAll(requestList.subList(k, (int) (k + i)));

           k = (int) (k + i + 1);
//           Здесь я оставил условие на дробную часть так чтобы в конце дораспределить по одной задаче на пользователя
//           Не надо еще раз бегать по листу и анализировать его
           if (requestList.size() - k < j) {
               user.getRequestList().add(requestList.get(k));
               ++k;
           }
        }

    }
}
