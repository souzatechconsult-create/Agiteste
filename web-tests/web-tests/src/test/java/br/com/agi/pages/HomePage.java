package br.com.agi.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // URL base
    private static final String URL = "https://blogdoagi.com.br/";

    // Locators
    private final By searchIcon        = By.cssSelector("button.search-toggle, .search-icon, [aria-label='Buscar'], .icon-search");
    private final By searchInput       = By.cssSelector("input[type='search'], input.search-field, input[name='s']");
    private final By searchButton      = By.cssSelector("button[type='submit'], input[type='submit'], .search-submit");
    private final By searchResults     = By.cssSelector(".search-results article, .post, article.post");
    private final By noResultsMessage  = By.cssSelector(".no-results, .not-found, .search-no-results");
    private final By resultLinks       = By.cssSelector(".search-results article a, article.post a, h2.entry-title a");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void abrirPagina() {
        driver.get(URL);
    }

    public void clicarIconeBusca() {
        wait.until(ExpectedConditions.elementToBeClickable(searchIcon)).click();
    }

    public void digitarTermoBusca(String termo) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(termo);
    }

    public void submeterBusca() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        btn.click();
    }

    public boolean resultadosExibidos() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(searchResults));
            return !driver.findElements(searchResults).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean mensagemSemResultadoExibida() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessage));
            return true;
        } catch (Exception e) {
            // Alguns temas exibem no título da página
            return driver.getTitle().toLowerCase().contains("nenhum resultado")
                || driver.getPageSource().toLowerCase().contains("nenhum resultado")
                || driver.getPageSource().toLowerCase().contains("no results");
        }
    }

    public int contarResultados() {
        return driver.findElements(searchResults).size();
    }

    public String obterUrlPrimeiroResultado() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(resultLinks))
                   .getAttribute("href");
    }

    public void clicarPrimeiroResultado() {
        wait.until(ExpectedConditions.elementToBeClickable(resultLinks)).click();
    }

    public String obterUrlAtual() {
        return driver.getCurrentUrl();
    }

    public String obterTituloPagina() {
        return driver.getTitle();
    }

    public boolean paginaCarregada() {
        return driver.getCurrentUrl().contains("blogdoagi.com.br");
    }
}
