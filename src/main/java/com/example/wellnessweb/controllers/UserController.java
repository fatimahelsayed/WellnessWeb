package com.example.wellnessweb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Customer;

import aj.org.objectweb.asm.Attribute;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/profile")
public class UserController {

    @GetMapping("")
    public ModelAndView getProfile(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
            ModelAndView mav = new ModelAndView("userProfile.html");
            mav.addObject("customer", loggedInUser);
            return mav;
        }
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("editaccount")
    public ModelAndView getUpdateAccountForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("userProfileEdit.html");
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        mav.addObject("customer", loggedInUser);
        return mav;
    }

}
