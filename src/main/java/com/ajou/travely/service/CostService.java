package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.domain.Cost;
import com.ajou.travely.domain.Travel;
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

import java.util.Map;


@RequiredArgsConstructor
@Service
public class CostService {
    private final CostRepository costRepository;

    private final UserRepository userRepository;

    private final UserCostRepository userCostRepository;

    private final TravelRepository travelRepository;

    @Transactional
    public CostCreateResponseDto createCost(Long totalAmount, Long travelId, String title, String content, Boolean isEquallyDivided, Map<Long, Long> amountsPerUser, Long payerId) {
        // 여행 객체 생성
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                , ErrorCode.TRAVEL_NOT_FOUND
                ));
        // 결제자 객체 생성
        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 User가 존재하지 않습니다."
                        , ErrorCode.USER_NOT_FOUND
                ));
        // 지출 객체 생성
        Cost cost = Cost.builder()
                .totalAmount(totalAmount)
                .content(content)
                .title(title)
                .travel(travel)
                .payerId(payerId)
                .isEquallyDivided(isEquallyDivided)
                .build();
        // 사용자_지출 객체 생성
        for (Long userId : amountsPerUser.keySet()) {
            UserCost userCost = UserCost.builder()
                    .cost(cost)
                    .user(userRepository.findById(userId)
                            .orElseThrow(() -> new RecordNotFoundException(
                                    "해당 ID의 User가 존재하지 않습니다."
                                    , ErrorCode.USER_NOT_FOUND
                            )))
                    .amount(amountsPerUser.get(userId))
                    .build();
            userCostRepository.save(userCost);
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
}
