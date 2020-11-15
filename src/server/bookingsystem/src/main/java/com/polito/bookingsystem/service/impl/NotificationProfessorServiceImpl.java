package com.polito.bookingsystem.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polito.bookingsystem.converter.NotificationProfessorConverter;
import com.polito.bookingsystem.converter.ProfessorConverter;
import com.polito.bookingsystem.dto.NotificationProfessorDto;
import com.polito.bookingsystem.dto.ProfessorDto;
import com.polito.bookingsystem.entity.NotificationProfessor;
import com.polito.bookingsystem.entity.Professor;
import com.polito.bookingsystem.repository.NotificationProfessorRepository;
import com.polito.bookingsystem.repository.ProfessorRepository;
import com.polito.bookingsystem.service.NotificationProfessorService;

@Service
public class NotificationProfessorServiceImpl implements NotificationProfessorService {
	@Autowired
	NotificationProfessorRepository notificationProfessorRepository;
	
	@Autowired
	ProfessorRepository professorRepository;

	@Override
	public boolean sendProfessorNotification(ProfessorDto professorDto, String description, String link) {
		Professor professor = ProfessorConverter.toEntity(professorDto);
		if(professorRepository.findById(professorDto.getUserId()) != null) {
			NotificationProfessor notificationProfessor= new NotificationProfessor();
			notificationProfessor.setDate(new Date());
			notificationProfessor.setDescription(description);
			notificationProfessor.setProfessor(professor);
			notificationProfessor.setLink(link);
			notificationProfessor.setStatus(false);
			if(notificationProfessorRepository.save(notificationProfessor) != null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<NotificationProfessorDto> getProfessorNotifications(ProfessorDto professorDto) {
		if(professorRepository.findById(professorDto.getUserId()) != null) {
			List<NotificationProfessor> notificationProfessorList= notificationProfessorRepository.findByProfessor(ProfessorConverter.toEntity(professorDto));
			
			return NotificationProfessorConverter.toDto(notificationProfessorList);
		}
		return null;
	}

	@Override
	public boolean setNotificationAsRead(NotificationProfessorDto notificationProfessorDto) {
		if(notificationProfessorRepository.findById(notificationProfessorDto.getNotificationId()) != null) {
			notificationProfessorDto.setStatus(true);
			NotificationProfessor notificationProfessor = NotificationProfessorConverter.toEntity(notificationProfessorDto);
			notificationProfessorRepository.save(notificationProfessor);
			return true;
		}
		return false;
	}

}
