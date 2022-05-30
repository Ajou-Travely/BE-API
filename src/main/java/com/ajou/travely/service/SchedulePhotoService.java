package com.ajou.travely.service;

import com.ajou.travely.repository.SchedulePhotoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SchedulePhotoService {

    private final SchedulePhotoRepository schedulePhotoRepository;



}
