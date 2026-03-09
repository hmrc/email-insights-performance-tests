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

package uk.gov.hmrc.perftests.insights.service

import io.gatling.http.Predef.HttpHeaderNames
import play.api.libs.json.{JsString, Json}
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.perftests.insights.InsightsRequests.baseUrlFor
import uk.gov.hmrc.perftests.insights.client.HttpClientHelper
import uk.gov.hmrc.perftests.insights.utils.Logging

trait WatchlistTestOnlyDataService extends HttpClientHelper with Logging {

  val baseUrl: String = baseUrlFor("email-insights-proxy")


  def headers: Seq[(String, String)] =
    Seq(
      HttpHeaderNames.ContentType.toString -> "application/json",
      HttpHeaderNames.UserAgent.toString   -> "email-insights-performance-tests",
      "X-Correlation-ID"                   -> "performance-tests"
    )

  private def readEmailsFromFeederFile(): Seq[String] = {
    val source = scala.io.Source.fromResource("data/emails.csv")
      try
        source
          .getLines()
          .drop(1)
          .map(_.split(","))
          .collect { case Array(email, risk, _) if risk.trim != "0" => email.trim }
          .toSeq
      finally source.close()
    }

  def createWatchlistEmails(numberOfGeneratedEmails: Int): Unit = {
    val emails = readEmailsFromFeederFile()
    val payload = Json.obj(
      "generatedEntries" -> Json.obj("numberOfEntries" -> numberOfGeneratedEmails),
      "manualEntries" -> Json.obj("emailAddresses" -> emails)
    )
    val request = Json.stringify(payload)
    val response: StandaloneWSResponse =
      post(s"$baseUrl/email-insights-proxy/test-only/watchlist/data/create", request, headers: _*)

    logger.info(s"Inserted emails to watchlist, response status: ${response.status} and body: ${response.body}")
  }

  def deleteWatchlistEmails(): Unit = {
    val response: StandaloneWSResponse =
      delete(s"$baseUrl/email-insights-proxy/test-only/watchlist/data/delete", headers: _*)

    logger.info(s"Deleted emails from watchlist, response status: ${response.status} and body: ${response.body}")
  }

  def createGraphData(numberOfRandomEmails: Int, batchSize: Int): Unit = {
    val emails = readEmailsFromFeederFile()
    val vertexRecords = Json.arr(
      Json.obj(
        "vertexId" -> 1,
        "attributeId" -> JsString(emails.headOption.getOrElse("")),
        "data" -> "{}",
        "vertexType" -> "email",
        "hopsToClosestRisky" -> 1
      )
    )
    val payload = Json.obj(
      "randomEntriesToGenerate" -> numberOfRandomEmails,
      "batchInsertSize" -> batchSize,
      "vertexRecords" -> vertexRecords
    )
    val request = Json.stringify(payload)
    val response: StandaloneWSResponse =
      post(s"$baseUrl/test-only/cip-risk/str/vertex-data", request, headers: _*)

    val message = (Json.parse(response.body) \ "message").asOpt[String].getOrElse("No message found")
    logger.info(s"Inserted emails into graph testonly endpoint, response status: ${response.status} and body: {\"message\":\"$message\"}")
  }

  def deleteGraphDataEmails(): Unit = {
    val response: StandaloneWSResponse =
      delete(s"$baseUrl/test-only/cip-risk/str/vertex-data", headers: _*)

    logger.info(s"Deleted emails from graph testonly endpoint, response status: ${response.status} and body: ${response.body}")
  }
}
