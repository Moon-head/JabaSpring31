package by.kremen.theatre.controller;

import by.kremen.theatre.model.Performance;
import by.kremen.theatre.model.Review;
import by.kremen.theatre.model.User;
import by.kremen.theatre.repository.PerformanceRepository;
import by.kremen.theatre.repository.ReviewRepository;
import by.kremen.theatre.repository.UserRepository;
import by.kremen.theatre.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private UserRepository userRepository;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReviewController.class);

    @PostMapping("/review")
    public String addUser(@RequestParam String perf_id, Review review, Model model, @AuthenticationPrincipal User user){

        //Performance perf = (Performance)model.getAttribute("performance");
        //int perf_id = (int)model.getAttribute("id");

        review.setUser(user);
        System.out.println(perf_id);
        //System.out.println(perf.getId());
        //System.out.println(perf.getTitle());

        review.setPerformance(performanceRepository.findById(Integer.parseInt(perf_id)));
        reviewRepository.save(review);

        String text =" Your review " + review.getTitle() + ": " + review.getBody() + " was posted to " + review.getPerformance().getTitle();

        MailSender mailSender = new MailSender();
        mailSender.Send("Thanks for you review", text, user.getEmail());

        log.info("add review");
        return "redirect:/main";
    }

    @GetMapping("/review")
    public String main(@RequestParam String perf_id, @AuthenticationPrincipal User user, Model model) {
        Long idL = Long.getLong(perf_id);
        //Performance performance = performanceRepository.findById(idL);
        Performance performance = performanceRepository.findById(Integer.parseInt(perf_id));
        model.addAttribute("performance", performance);
        model.addAttribute("id", perf_id);

        log.info("get add review");
        return "review";
    }


    @GetMapping("/myreviews")
    public String myreviews(@AuthenticationPrincipal User user, Model model) {

        List<Review> reviews = reviewRepository.findAllByUser(user);

        model.addAttribute("reviews", reviews);
        model.addAttribute("user", user);
        model.addAttribute("owner", true);

        log.info("get reviews of user");
        return "reviews";
    }

    @GetMapping("/reviews")
    public String reviews(@AuthenticationPrincipal User user, Model model) {

        List<Review> reviews = reviewRepository.findAll();

        model.addAttribute("reviews", reviews);
        model.addAttribute("user", user);

        log.info("get all reviews");
        return "reviews";
    }

    @GetMapping("/reviewsPerf")
    public String reviewsPerf(@RequestParam String perf_id, @AuthenticationPrincipal User user, Model model) {


        List<Review> reviews = reviewRepository.findAllByPerformance(performanceRepository.findById(Integer.parseInt(perf_id)));

        model.addAttribute("reviews", reviews);
        model.addAttribute("user", user);

        log.info("add review on performance");
        return "reviews";
    }

    @GetMapping("/reviewDel")
    public String mainDel(@RequestParam String rev_id, @AuthenticationPrincipal User user, Model model) {
       /* if(user.getAuthorities()!= Collections.singleton(Role.ADMIN)){

            model.addAttribute("message", "If you want add dishes you have to be an ADMIN");
            return "login";
        }*/

        //Long idL = Long.getLong(perf_id);
        //Performance performance = performanceRepository.findById(idL);
        reviewRepository.delete(reviewRepository.findById(Integer.parseInt(rev_id)));

        log.info("delete review");

        return "redirect:/myreviews";
    }
}
