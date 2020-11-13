package com.polito.bookingsystem.converter;

import java.util.ArrayList;
import java.util.List;

import com.polito.bookingsystem.dto.CourseDto;
import com.polito.bookingsystem.dto.ProfessorDto;
import com.polito.bookingsystem.entity.Course;
import com.polito.bookingsystem.entity.Professor;


public class ProfessorConverter extends UserConverter{
	
	public static Professor toEntity(ProfessorDto professorDto) {
		Professor professor = new Professor();
		professor.setAddress(professorDto.getAddress());
		professor.setEmail(professorDto.getEmail());
		professor.setName(professorDto.getName());
		professor.setPassword(professorDto.getPassword());
		professor.setSurname(professorDto.getSurname());
		professor.setUserId(professorDto.getUserId());
		List<Course> courses = new ArrayList<Course>();
		for(CourseDto course : professorDto.getCoursesDto()) {
			  courses.add(CourseConverter.toEntity(course));
		}
		professor.setCourses(courses);
		return professor;
	}
	
	public static ProfessorDto toDto(Professor professor) {
		ProfessorDto professorDto = new ProfessorDto();
		professorDto.setAddress(professor.getAddress());
		professorDto.setEmail(professor.getEmail());
		professorDto.setName(professor.getName());
		professorDto.setPassword(professor.getPassword());
		professorDto.setSurname(professor.getSurname());
		professorDto.setUserId(professor.getUserId());
		List<CourseDto> coursesDto = new ArrayList<CourseDto>();
		for(Course courseDto : professor.getCourses()) {
			  coursesDto.add(CourseConverter.toDto(courseDto));
		}
		professorDto.setCoursesDto(coursesDto);
		return professorDto;
	}

	
}