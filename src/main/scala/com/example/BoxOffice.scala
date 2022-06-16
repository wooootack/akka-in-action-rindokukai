package com.example

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext}


object BoxOffice {
  sealed trait Command
}

class BoxOffice(context: ActorContext[BoxOffice.Command])
  extends AbstractBehavior[BoxOffice.Command](context) {
  import BoxOffice._
}
