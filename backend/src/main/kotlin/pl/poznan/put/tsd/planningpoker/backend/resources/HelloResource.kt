package pl.poznan.put.tsd.planningpoker.backend.resources

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloResource {

    @GetMapping("/hello")
    fun hello() = "Hello world!"
}