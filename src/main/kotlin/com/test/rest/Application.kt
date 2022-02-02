package com.test.rest

import com.fasterxml.jackson.databind.SerializationFeature
import com.test.rest.modules.analytics.AnalyticsProvider
import com.test.rest.modules.constructor.ConstructorProvider
import com.test.rest.modules.general.MainInfoProvider
import com.test.rest.modules.messaging.RestMessagingProvider
import com.test.rest.modules.referrers.RestReferrersProvider
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class Application {

    //todo Очень много моделей данных, связанных с данными бота
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            initBots()
            startServer()
        }

        private fun initBots() {
            com.test.rest.BotsProvider.init()
        }

        private fun startServer() {
            val server = embeddedServer(
                Netty,
//                port = System.getenv("PORT").toInt(),
                port = 11320
            ) {
                install(ContentNegotiation) {
                    jackson {
                        enable(SerializationFeature.INDENT_OUTPUT)
                    }
                }
                install(StatusPages) {
                    exception<Throwable> { cause ->
                        cause.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
                    }
                }
                install(CORS) {
                    method(HttpMethod.Get)
                    method(HttpMethod.Post)
                    method(HttpMethod.Delete)
                    anyHost()
                }
                routing {
                    get("test") {
                        call.respondText("Hello world")
                    }
                    route("v1") {
                        route("bots") {
                            get("list") {
                                MainInfoProvider.getBotsList(call)
                            }
                            post("update") {
                                MainInfoProvider.updateBotInfo(call)
                            }
                            post("create") {
                                MainInfoProvider.createBot(call)
                            }
                        }
                        route("bot") {
                            route("referrers") {
                                get("list") {
                                    RestReferrersProvider.getReferrersList(call)
                                }
                                post("add") {
                                    RestReferrersProvider.addReferrerLinks(call)
                                }
                                delete("delete") {
                                    RestReferrersProvider.deleteReferrerLink(call)
                                }
                                post("update") {
                                    RestReferrersProvider.updateReferrerLink(call)
                                }
                                post("users") {
                                    RestReferrersProvider.updateUserReferrerSettings(call)
                                }
                                get("info") {
                                    RestReferrersProvider.getReferrersInfo(call)
                                }
                            }
                            route("messaging") {
                                post("send") {
                                    RestMessagingProvider.sendMailing(call)
                                }
                                get("list") {
                                    RestMessagingProvider.getMailingList(call)
                                }
                                delete("delete") {
                                    RestMessagingProvider.deleteMailing(call)
                                }
                                post("update") {
                                    RestMessagingProvider.updateMessage(call)
                                }
                            }
                            route("scheduler") {
                                post("schedule") {
                                    RestMessagingProvider.savePlanningMailing(call)
                                }
                                get("list") {
                                    RestMessagingProvider.getPlanningMailing(call)
                                }
                                delete("delete") {
                                    RestMessagingProvider.deletePlanningMailing(call)
                                }
                                post("update") {
                                    RestMessagingProvider.updatePlanningMailing(call)
                                }
                            }
                            route("analytics") {
                                get("users") {
                                    AnalyticsProvider.getUsersAnalytics(call)
                                }
                            }
                        }
                        route("constructor") {
                            get("instructions") {
                                ConstructorProvider.getBotInstructions(call)
                            }
                            post("instructions") {
                                ConstructorProvider.updateInstructions(call)
                            }
                            post("image") {
                                ConstructorProvider.addNewImage(call)
                            }
                            get("image") {
                                ConstructorProvider.getImage(call)
                            }
                        }
                    }
                }
            }
            server.start(true)
        }
    }
}
