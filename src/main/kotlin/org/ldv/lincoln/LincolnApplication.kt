package org.ldv.lincoln

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LincolnApplication

fun main(args: Array<String>) {
    runApplication<LincolnApplication>(*args)
}
