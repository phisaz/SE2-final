package com.polito.bookingsystem.utils.serviceTests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.junit4.SpringRunner;
import com.polito.bookingsystem.dto.CourseDto;
import com.polito.bookingsystem.dto.LectureDto;
import com.polito.bookingsystem.dto.ProfessorDto;
import com.polito.bookingsystem.dto.RoomDto;
import com.polito.bookingsystem.dto.StudentDto;
import com.polito.bookingsystem.entity.Booking;
import com.polito.bookingsystem.entity.Course;
import com.polito.bookingsystem.entity.Lecture;
import com.polito.bookingsystem.entity.Professor;
import com.polito.bookingsystem.entity.Room;
import com.polito.bookingsystem.entity.Student;
import com.polito.bookingsystem.repository.BookingRepository;
import com.polito.bookingsystem.repository.RoomRepository;
import com.polito.bookingsystem.repository.LectureRepository;
import com.polito.bookingsystem.repository.CourseRepository;
import com.polito.bookingsystem.repository.ProfessorRepository;
import com.polito.bookingsystem.repository.StudentRepository;
import com.polito.bookingsystem.service.StudentService;
import com.polito.bookingsystem.service.LectureService;
import com.polito.bookingsystem.service.impl.LectureServiceImpl;
import com.polito.bookingsystem.service.impl.CourseServiceImpl;
import com.polito.bookingsystem.utils.BookingInfo;

@RunWith(SpringRunner.class)
@DataJpaTest
class CourseServiceTest {
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private LectureRepository lectureRepository;
	@Autowired
	private ProfessorRepository professorRepository;
	private LectureService lectureService;
	private CourseServiceImpl courseServiceImpl;
	
	@BeforeEach
	public void setUp() throws Exception {
		courseRepository = mock(CourseRepository.class);
		lectureRepository = mock(LectureRepository.class);
		professorRepository = mock(ProfessorRepository.class);
		lectureService = mock(LectureService.class);
		courseServiceImpl = new CourseServiceImpl(professorRepository,courseRepository,lectureRepository,lectureService);
	}
	
	@Test
	void testAddCourses1() {
		String fileName = "../../test-files/Courses.csv";
		
		
		Course course1 = new Course(1, "testName1", "A",1,1);
		Course course2 = new Course(2, "testName2", "B",1,1);
		Course course3 = new Course(3, "testName3", "C",1,1);
		
		
		List<Course> courses = new ArrayList<>();
		courses.add(course1);
		courses.add(course2);
		courses.add(course3);
		Professor professor1 = new Professor(1, "testName", "testSurname", "testAddress", "testProfessor@email.com", "testPassword",courses,"d0");
		try {
		     
			when(courseRepository.findByCode(anyObject())).thenReturn(course1).thenReturn(null);
			when(courseRepository.findAll()).thenReturn(courses);
			
			when(professorRepository.findByCode(anyObject())).thenReturn(null).thenReturn(professor1);
			when(courseRepository.save(anyObject())).thenReturn(null);
			when(professorRepository.save(anyObject())).thenReturn(null);
			courseServiceImpl.addCourses(fileName);
			
		} catch (Exception e) {
		          fail("should't come here. exception wrongly thrown");
		}
	}
	
	@Test
	void testAddCourses2() {
		String fileName = "test-wrong";
		IOException e= new IOException();
		try {
			courseServiceImpl.addCourses(fileName);
			
		}catch (Exception ex) {
			fail("Shouldn't come here");
		}
	}
	
	@Test
	void testGetCourses1() {
		when(courseRepository.findAll()).thenReturn(null);
		assertNull(courseServiceImpl.getCourses());
	}
	
	@Test
	void testGetCourses2() {
		List<Course> courses = new ArrayList<>();
		Course course1 = new Course(1, "testName1", "A",1,1);
		courses.add(course1);
		when(courseRepository.findAll()).thenReturn(courses);
		assertEquals("expect same length", courseServiceImpl.getCourses().size(),courses.size());
	}
	
	@Test
	void testSetCourseToRemote1() {
		String courseName = "notexists";
		when(courseRepository.findByCode(anyObject())).thenReturn(null);
		when(lectureService.getListAllLectures()).thenReturn(null);
		courseServiceImpl.setCourseToRemote(courseName);
	}
	
	@Test
	void testSetCourseToRemote2() throws ParseException {
		//course is already remote
		String courseName = "my-course";
		RoomDto room1 = new RoomDto(1, "testName", 100);
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/0101");
		Course course1 = new Course(1, "testName", "A", 1,1);
		CourseDto courseDto1 = new CourseDto(1, "testName", "A", 1,1);
		CourseDto courseDto2 = new CourseDto(2, "testName2", "A", 1,1);
		List<CourseDto> courses = new ArrayList<>();
		List<LectureDto> lectures = new ArrayList<>();
		ProfessorDto professorDto1 = new ProfessorDto(1, "testName", "testSurname", "testAddress", "test@email.com", "testPassword", courses, "d0");		 
		//lecture is already remote
		LectureDto lectureDto1 = new LectureDto(1, 10, courseDto1, professorDto1, true, date, 90, "testDetails", room1);
		//lecture for another course
		LectureDto lectureDto2 = new LectureDto(2, 10, courseDto2, professorDto1, false, date, 90, "testDetails", room1);
		//lecture is deleted
		LectureDto lectureDto3 = new LectureDto(3, 10, courseDto1, professorDto1, false, date, 90, "testDetails", room1);
		lectureDto3.setDeleted(true);
		lectures.add(lectureDto3);
		lectures.add(lectureDto2);
		lectures.add(lectureDto1);
		when(lectureService.getListAllLectures()).thenReturn(lectures);
		when(courseRepository.findByCode(anyObject())).thenReturn(course1);
		courseServiceImpl.setCourseToRemote(courseName);
	}
	
	@Test
	void testSetCourseToRemote3() throws ParseException {
		//lecture is expired
		String courseName = "my-course";
		RoomDto room1 = new RoomDto(1, "testName", 100);
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2020");
		Course course1 = new Course(1, "testName", "A", 1,1);
		CourseDto courseDto1 = new CourseDto(1, "testName", "A", 1,1);
		CourseDto courseDto2 = new CourseDto(2, "testName2", "A", 1,1);
		List<CourseDto> courses = new ArrayList<>();
		List<LectureDto> lectures = new ArrayList<>();
		ProfessorDto professorDto1 = new ProfessorDto(1, "testName", "testSurname", "testAddress", "test@email.com", "testPassword", courses, "d0");		 
		LectureDto lectureDto1 = new LectureDto(1, 10, courseDto1, professorDto1, false, date, 90, "testDetails", room1);
		LectureDto lectureDto2 = new LectureDto(1, 10, courseDto2, professorDto1, false, date, 90, "testDetails", room1);
		lectures.add(lectureDto2);
		lectures.add(lectureDto1);
		when(lectureService.getListAllLectures()).thenReturn(lectures);
		when(courseRepository.findByCode(anyObject())).thenReturn(course1);
		courseServiceImpl.setCourseToRemote(courseName);
	}
	
	@Test
	void testSetCourseToRemote4() throws ParseException {
		//lecture should be moved to remote
		String courseName = "my-course";
		RoomDto room1 = new RoomDto(1, "testName", 100);
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2021");
		Course course1 = new Course(1, "testName", "A", 1,1);
		CourseDto courseDto1 = new CourseDto(1, "testName", "A", 1,1);
		CourseDto courseDto2 = new CourseDto(2, "testName2", "B", 1,1);
		List<CourseDto> courses = new ArrayList<>();
		List<LectureDto> lectures = new ArrayList<>();
		ProfessorDto professorDto1 = new ProfessorDto(1, "testName", "testSurname", "testAddress", "test@email.com", "testPassword", courses, "d0");		 
		LectureDto lectureDto1 = new LectureDto(1, 10, courseDto1, professorDto1, false, date, 90, "testDetails", room1);
		LectureDto lectureDto2 = new LectureDto(1, 10, courseDto2, professorDto1, false, date, 90, "testDetails", room1);
		lectures.add(lectureDto2);
		lectures.add(lectureDto1);
		when(lectureService.getListAllLectures()).thenReturn(lectures);
		when(courseRepository.findByCode(anyObject())).thenReturn(course1);
		when(lectureRepository.save(anyObject())).thenReturn(null);
		courseServiceImpl.setCourseToRemote(courseName);
	}
	
	
	
	

}
