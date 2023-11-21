package com.example

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AnnotationController {
    @GetMapping("annotation")
    fun say() =
        "Hello wold"
}