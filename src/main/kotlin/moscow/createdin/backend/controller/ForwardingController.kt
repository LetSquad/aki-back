@file:Suppress("MVCPathVariableInspection")

package moscow.createdin.backend.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Пользователь может напрямую вбивать в адресную строку пути и будет получать 404 ошибку от веб сервера,
 * так как существует только index.html, остальные страницы реализованы на react.
 * Чтобы уйти от этой проблемы, добавлен контроллер, перенаправляющий все запросы к корневой странице,
 * а далее маршрутизирует react router.
 * Запросы к бэкэнд контроллерам не перенаправляются.
 */
@Controller
class ForwardingController {

    @RequestMapping(value = ["/{x:[\\w\\-]+}", "/{x:^(?!api$).*$}/**/{y:[\\w\\-]+}"])
    fun forward(): String {
        return "forward:/"
    }
}
