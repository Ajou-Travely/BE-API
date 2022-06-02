package com.ajou.travely.service;

import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.cost.dto.*;
import com.ajou.travely.domain.kakao.KakaoMessageResponse;
import com.ajou.travely.domain.cost.Cost;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.CostRepository;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserCostRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.*;


@RequiredArgsConstructor
@Service
public class CostService {
    private final CostRepository costRepository;

    private final UserRepository userRepository;

    private final UserCostRepository userCostRepository;

    private final TravelRepository travelRepository;

    private final KakaoApiService kakaoApiService;

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public CostCreateResponseDto createCost(CostCreateRequestDto requestDto, Long travelId) {
        // 여행 객체 생성
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                , ErrorCode.TRAVEL_NOT_FOUND
                ));
        // 결제자 객체 생성
        User payer = userRepository.findById(requestDto.getPayerId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 User가 존재하지 않습니다."
                        , ErrorCode.USER_NOT_FOUND
                ));
        // 지출 객체 생성
        Cost cost = Cost.builder()
                .totalAmount(requestDto.getTotalAmount())
                .content(requestDto.getContent())
                .title(requestDto.getTitle())
                .travel(travel)
                .payerId(requestDto.getPayerId())
                .build();
        // 사용자_지출 객체 생성
        for (Long userId : requestDto.getAmountsPerUser().keySet()) {
            UserCost userCost = UserCost.builder()
                    .cost(cost)
                    .user(userRepository.findById(userId)
                            .orElseThrow(() -> new RecordNotFoundException(
                                    "해당 ID의 User가 존재하지 않습니다."
                                    , ErrorCode.USER_NOT_FOUND
                            )))
                    .amount(requestDto.getAmountsPerUser().get(userId))
                    .build();
            cost.addUserCost(userCost);
        }
        costRepository.save(cost);

        return new CostCreateResponseDto(cost, payer);
    }

    @Transactional(readOnly = true)
    public CostResponseDto getCostById(Long costId) {
        Cost cost = costRepository.getCostById(costId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Cost이 존재하지 않습니다."
                        , ErrorCode.COST_NOT_FOUND
                ));

        return new CostResponseDto(cost);
    }

    @Transactional
    public CostResponseDto updateCostById(Long costId, CostUpdateDto costUpdateDto) {
        Cost cost = costRepository.findById(costId)
                .orElseThrow(() -> new RuntimeException("해당 지출이 존재하지 않습니다."));
        cost.updateCost(costUpdateDto);
        List<UserCost> userCosts = cost.getUserCosts();
        if (!costUpdateDto.getAmountsPerUser().keySet().isEmpty()) {
            Map<Long, UserCost> exAmountsPerUser = new HashMap<>();
            userCosts.forEach(userCost -> exAmountsPerUser.put(
                    userCost.getUser().getId(),
                    userCost
            ));
            Set<Long> userToDelete = new HashSet<>(exAmountsPerUser.keySet());
            Set<Long> userToAdd = new HashSet<>(costUpdateDto.getAmountsPerUser().keySet());
            Set<Long> userToStay = new HashSet<>(userToAdd);
            userToStay.retainAll(userToDelete);
            userToDelete.removeAll(userToAdd);
            userToAdd.removeAll(userToStay);


            userToDelete.forEach(userId -> userCostRepository.deleteUserCostByCostIdAndUserId(costId, userId));


            userToAdd.forEach(userId -> userCostRepository.save(new UserCost(
                    cost,
                    userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다.")),
                    costUpdateDto.getAmountsPerUser().get(userId).getAmount(),
                    costUpdateDto.getAmountsPerUser().get(userId).getIsRequested()
            )));

            userToStay.forEach(userId -> userCostRepository.updateUserCostByUserCostId(
                    costUpdateDto.getAmountsPerUser().get(userId).getAmount(),
                    costUpdateDto.getAmountsPerUser().get(userId).getIsRequested(),
                    exAmountsPerUser.get(userId).getId()
            ));
        }

        em.flush();
        em.clear();

        Cost returnCost = costRepository.findById(costId)
                .orElseThrow(() -> new RuntimeException("해당 지출이 존재하지 않습니다."));
        return new CostResponseDto(returnCost);
    }
    @Transactional
    public void deleteCostById(Long costId) {
        costRepository.deleteById(costId);
    }

    @Transactional
    public KakaoMessageResponse calculateCost(Long userCostId, SessionUser sessionUser, CostCalculateRequestDto requestDto) {
        UserCost userCost = userCostRepository.findById(userCostId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 정산 사항이 존재하지 않습니다.",
                        ErrorCode.USER_COST_NOT_FOUND
                ));
        User user = userRepository.findById(sessionUser.getUserId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 유저가 존재하지 않습니다.",
                        ErrorCode.USER_NOT_FOUND
                ));
        String msg = "Travely\n" +
                "안녕하세요 " + userCost.getUser().getName() + "님!\n" +
                 user.getName() + " 님으로 부터\n" +
                 userCost.getAmount() + "원 정산이 요청되었습니다!";
        KakaoMessageResponse response = kakaoApiService.sendTextMessage(requestDto.getReceiverUuids(), msg, sessionUser.getAccessToken());
        userCost.isRequested();
        return response;
    }
}
