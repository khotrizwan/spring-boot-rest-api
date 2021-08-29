package com.khotrizwan.angular8.controller;
// Create Controller

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.khotrizwan.angular8.model.City;
import com.khotrizwan.angular8.model.ResponseBean;
import com.khotrizwan.angular8.services.CityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class CityController {

    Logger log = LoggerFactory.getLogger(CityController.class);

    @Autowired
    CityService service;

    @GetMapping("/city-list")
    public ResponseEntity<ResponseBean>  cityList(HttpServletRequest request)  {
        log.debug("Inside cityList");
        HttpSession session = request.getSession();
        session.setAttribute("Test", "Test Session");
        log.debug("" + session.getAttribute("Test"));
        System.out.println("Inside cityList");
        return setResponse(service.getAllCities(), "Data not faund");
    }
    @GetMapping("/city/{id}")
    public ResponseEntity<ResponseBean>  getCity(@PathVariable("id") long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.debug("Inside getCity");
        log.debug("" + session.getAttribute("Test"));
        if(session.getAttribute("Test") == null)
         return setResponse(null, "Invalid Session");

        System.out.println("Inside getCity");
        return setResponse(service.getCity(id), "Data not faund");
    }

    @PostMapping("/edit-city/{id}")
    public ResponseEntity<ResponseBean>  editCity(@PathVariable("id") long id, @RequestBody City city, HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.debug("Inside editCity");
        log.debug("" + session.getAttribute("Test"));
        if(session.getAttribute("Test") == null)
         return setResponse(null, "Invalid Session");

        log.debug("Inside editCity:" + id + ":" + city.getId());
        System.out.println("Inside editCity:" + id + ":" + city.getId());
        if(id == city.getId() && service.getCity(id) != null) {
            return setResponse(service.save(city), "Updation Failed");
        }
        
        log.debug("Edit City Done");
        return setResponse(null, "Invaild details");
    }

    @PostMapping("/add-city")
    public ResponseEntity<ResponseBean> addCity(@RequestBody City city, HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.debug("Inside addCity");
        log.debug("" + session.getAttribute("Test"));
        if(session.getAttribute("Test") == null)
         return setResponse(null, "Invalid Session");

        log.debug("Inside addCity " + city);
        System.out.println("Inside addCityt");
        return setResponse(service.save(city), "Additiomn Failed");
    }
    
    private ResponseEntity<ResponseBean> setResponse(Object object, String errorMessage) {
        ResponseBean responseBean = null;
        HttpStatus status= HttpStatus.BAD_REQUEST;
        if(object == null) {
            responseBean = new ResponseBean("FAILED", errorMessage, object);
            status = HttpStatus.NOT_FOUND;
        } else {
            responseBean = new ResponseBean("SUCCESS", "SUCCESS", object);
            status = HttpStatus.OK;
        }
        log.debug(responseBean.toString());
        return new ResponseEntity<>(responseBean, status);
    }
    
}