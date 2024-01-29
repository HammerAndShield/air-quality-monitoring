package org.hammernshield.webform

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class GreetingController {

    @GetMapping("/")
    fun greetingForm(model: Model): String {
        return "greeting"
    }

    @PostMapping("/")
    fun greetingSubmit(@RequestParam name: String, model: Model): String {
        model.addAttribute("name", name)
        return "result"
    }

}