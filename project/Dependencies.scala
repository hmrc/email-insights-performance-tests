/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "com.typesafe" % "config"                  % "1.4.5" % Test,
    "uk.gov.hmrc" %% "performance-test-runner" % "6.3.0" % Test,
    "uk.gov.hmrc" %% "api-test-runner"         % "0.10.0"
  )
}
