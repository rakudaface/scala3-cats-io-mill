package com.example

import cats.effect.IO

object Greeter:
  def greet: IO[Unit] =
    for {
      greeting <- IO.delay(HelloPrinter.message("CatsIO"))
      _ <- IO.println(greeting)
    } yield ()


