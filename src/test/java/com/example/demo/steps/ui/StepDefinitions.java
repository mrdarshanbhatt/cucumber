
package com.example.demo.steps.ui;

import com.example.demo.config.factory.DriverFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import junit.framework.Assert;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("deprecation")
public class StepDefinitions {

    WebDriver webDriver = DriverFactory.getDriver();

    @Given("^I am on the home page$")
    public void iAmOnTheHomePage() {
        webDriver.get("https://www.saucedemo.com/");
    }

    @Given("^I login in with the following details$")
    public void iLoginWithFollowingDetails(DataTable dataTable) {
        List<String> credentials = dataTable.row(1);
        String username = credentials.getFirst();
        String password = credentials.get(1);

        webDriver.findElement(By.id("user-name")).sendKeys(username);
        webDriver.findElement(By.id("password")).sendKeys(password);
        webDriver.findElement(By.id("login-button")).click();
    }

    @Given("^I add the following items to the basket$")
    public void iAddFollowingItemsToBasket(DataTable dataTable) {
        List<String> items = dataTable.asList();
        for (String item : items) {
            String buttonId = getAddToCartButtonId(item);
            webDriver.findElement(By.id(buttonId)).click();
        }
    }
    
    private String getAddToCartButtonId(String itemName) {
        switch (itemName) {
            case "Sauce Labs Backpack":
                return "add-to-cart-sauce-labs-backpack";
            case "Sauce Labs Fleece Jacket":
                return "add-to-cart-sauce-labs-fleece-jacket";
            case "Sauce Labs Bolt T-Shirt":
                return "add-to-cart-sauce-labs-bolt-t-shirt";
            case "Sauce Labs Onesie":
                return "add-to-cart-sauce-labs-onesie";
            default:
                throw new IllegalArgumentException("Unknown item: " + itemName);
        }
    }

    @Given("^I should see (\\d+) items added to the shopping cart$")
    public void iShouldSeeItemsInShoppingCart(int expectedCount) {
        String cartBadge = webDriver.findElement(By.className("shopping_cart_badge")).getText();
        int actualCount = Integer.parseInt(cartBadge);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Given("^I click on the shopping cart$")
    public void iClickOnShoppingCart() {
        webDriver.findElement(By.className("shopping_cart_link")).click();
    }

    @Given("^I verify that the QTY count for each item should be (\\d+)$")
    public void iVerifyQtyCountForEachItem(int expectedQty) {
        List<org.openqa.selenium.WebElement> qtyElements = webDriver.findElements(By.className("cart_quantity"));
        for (org.openqa.selenium.WebElement qtyElement : qtyElements) {
            int actualQty = Integer.parseInt(qtyElement.getText());
            Assert.assertEquals(expectedQty, actualQty);
        }
    }

    @Given("^I remove the following item:$")
    public void iRemoveFollowingItem(DataTable dataTable) {
        List<String> items = dataTable.asList();
        for (String item : items) {
            String removeButtonId = getRemoveButtonId(item);
            webDriver.findElement(By.id(removeButtonId)).click();
        }
    }

    private String getRemoveButtonId(String itemName) {
        switch (itemName) {
            case "Sauce Labs Backpack":
                return "remove-sauce-labs-backpack";
            case "Sauce Labs Fleece Jacket":
                return "remove-sauce-labs-fleece-jacket";
            case "Sauce Labs Bolt T-Shirt":
                return "remove-sauce-labs-bolt-t-shirt";
            case "Sauce Labs Onesie":
                return "remove-sauce-labs-onesie";
            default:
                throw new IllegalArgumentException("Unknown item: " + itemName);
        }
    }

    @Given("^I click on the CHECKOUT button$")
    public void iClickOnCheckoutButton() {
        webDriver.findElement(By.id("checkout")).click();
    }

    @Given("^I type \"(.*)\" for First Name$")
    public void iTypeForFirstName(String firstName) {
        webDriver.findElement(By.id("first-name")).sendKeys(firstName);
    }

    @Given("^I type \"(.*)\" for Last Name$")
    public void iTypeForLastName(String lastName) {
        webDriver.findElement(By.id("last-name")).sendKeys(lastName);
    }

    @Given("^I type \"(.*)\" for ZIP/Postal Code$")
    public void iTypeForZipPostalCode(String zipCode) {
        webDriver.findElement(By.id("postal-code")).sendKeys(zipCode);
    }

    @When("^I click on the CONTINUE button$")
    public void iClickOnContinueButton() {
        webDriver.findElement(By.id("continue")).click();
    }

    @Then("^Item total will be equal to the total of items on the list$")
    public void itemTotalWillEqualTotalOfItems() {
        String itemTotalText = webDriver.findElement(By.className("summary_subtotal_label")).getText();
        double itemTotal = Double.parseDouble(itemTotalText.replace("Item total: $", ""));
        
        List<org.openqa.selenium.WebElement> priceElements = webDriver.findElements(By.className("inventory_item_price"));
        double calculatedTotal = 0.0;
        for (org.openqa.selenium.WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace("$", "");
            calculatedTotal += Double.parseDouble(priceText);
        }
        Assert.assertEquals(calculatedTotal, itemTotal, 0.01);
    }

    @Then("^a Tax rate of (\\d+) % is applied to the total$")
    public void aTaxRateIsAppliedToTotal(int expectedTaxRate) {
        String itemTotalText = webDriver.findElement(By.className("summary_subtotal_label")).getText();
        double itemTotal = Double.parseDouble(itemTotalText.replace("Item total: $", ""));
        
        String taxText = webDriver.findElement(By.className("summary_tax_label")).getText();
        double actualTax = Double.parseDouble(taxText.replace("Tax: $", ""));
        
        double expectedTax = itemTotal * (expectedTaxRate / 100.0);
        Assert.assertEquals(expectedTax, actualTax, 0.01);
    }
}
