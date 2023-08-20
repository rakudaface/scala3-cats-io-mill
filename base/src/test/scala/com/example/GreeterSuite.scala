package com.example

import munit.CatsEffectSuite

class GreeterSuite extends CatsEffectSuite {

  test("test hello world says hi") {
    assertIO_(Greeter.greet)
  }

}
