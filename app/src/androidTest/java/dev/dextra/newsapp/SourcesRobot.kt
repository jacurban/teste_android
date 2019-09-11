package dev.dextra.newsapp

import dev.dextra.newsapp.api.model.enums.Category
import dev.dextra.newsapp.api.model.enums.Country
import dev.dextra.newsapp.base.*

class SourcesRobot {

    fun clickOnCountry() = clickOn(R.id.country_select)

    fun clickOnBrazil() = clickOnCountryPopupItem(Country.BR)

    fun clickOnUnitedStates() = clickOnCountryPopupItem(Country.US)

    fun clickOnCanada() = clickOnCountryPopupItem(Country.CA)

    fun checkSourceListIsDisplayed() =
        checkIfViewIsDisplayed(R.id.sources_list)

    fun checkErrorStateIsDisplayed() =
        checkIfViewIsDisplayed(R.id.error_state)

    fun checkEmptyStateIsDisplayed() =
        checkIfViewIsDisplayed(R.id.empty_state)

    fun checkSourceListNotDisplayed() =
        checkIfViewNotDisplayed(R.id.sources_list)

    fun checkErrorStateNotDisplayed() =
        checkIfViewNotDisplayed(R.id.error_state)

    fun checkEmptyStateNotDisplayed() =
        checkIfViewNotDisplayed(R.id.empty_state)

    fun checkBrazilTextWithDisplayed() = checkMatchText("Test Brazil")

    fun clickOnRetryErrorState() = clickOn(R.id.error_state_retry)

    fun clickOnCategory() = clickOn(R.id.category_select)

    fun clickOnBusiness() = clickOnCategoryPopupItem(Category.BUSINESS)
}