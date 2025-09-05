package com.utube.utube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.utube.utube.entity.Profile;
import com.utube.utube.entity.User;
import com.utube.utube.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public ModelAndView registerPage() {
        return new ModelAndView("register.html");
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute User user, RedirectAttributes attributes) {
        String encodePassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        try {
            this.userService.saveUser(user);
            attributes.addFlashAttribute("successMsg", "User Saved...");
            return new ModelAndView(new RedirectView("/register"));
        } catch (Exception e) {
            attributes.addFlashAttribute("errorMsg", "Something went wrong...");
            return new ModelAndView(new RedirectView("/register"));
        }
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login.html");
    }

    @GetMapping("/profile")
    public ModelAndView profilePage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return new ModelAndView(new RedirectView("/login"));
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Fetch User + Profile
        User user = userService.getUser(username);
        Profile profile = user.getProfile();

        // If profile doesn’t exist yet, create an empty one (so form fields don’t
        // break)
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
        }

        ModelAndView mv = new ModelAndView("profile");
        mv.addObject("profile", profile);
        return mv;
    }

    @PostMapping("/profile")
    public RedirectView saveProfile(@ModelAttribute Profile profile, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // find user
        User user = userService.getUser(username);

        // if profile already exists, update fields
        if (user.getProfile() != null) {
            Profile existing = user.getProfile();
            existing.setFullName(profile.getFullName());
            existing.setPhone(profile.getPhone());
            existing.setAddress(profile.getAddress());
            existing.setBio(profile.getBio());
            // keep same user
        } else {
            // new profile
            user.setProfile(profile); // sets both ways
        }

        // just save user -> profile will also be saved because of cascade
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return new RedirectView("/profile");
    }

    @GetMapping("/getPremium")
    public ModelAndView resultPage() {
        return new ModelAndView("premium-page.html");
    }

}
