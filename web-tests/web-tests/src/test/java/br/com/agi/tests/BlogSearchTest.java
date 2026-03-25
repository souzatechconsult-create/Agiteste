package br.com.agi.tests;

import br.com.agi.config.DriverConfig;
import br.com.agi.pages.HomePage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Blog do Agi - Testes de Busca de Artigos")
class BlogSearchTest {

    private static HomePage homePage;

    @BeforeAll
    static void configurar() {
        homePage = new HomePage(DriverConfig.getDriver());
    }

    @AfterAll
    static void encerrar() {
        DriverConfig.quitDriver();
    }

    // -------------------------------------------------------------------------
    // Cenário 1: Busca com termo válido retorna resultados
    // -------------------------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("CT001 - Busca com termo válido deve exibir artigos relacionados")
    void buscaComTermoValidoDeveRetornarResultados() {
        homePage.abrirPagina();

        assertTrue(homePage.paginaCarregada(),
            "A página do Blog do Agi deve estar acessível");

        homePage.clicarIconeBusca();
        homePage.digitarTermoBusca("investimento");
        homePage.submeterBusca();

        assertTrue(homePage.resultadosExibidos(),
            "Deve exibir ao menos um artigo para a busca por 'investimento'");

        assertTrue(homePage.contarResultados() > 0,
            "A quantidade de resultados deve ser maior que zero");
    }

    // -------------------------------------------------------------------------
    // Cenário 2: Busca com termo sem resultado exibe mensagem adequada
    // -------------------------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("CT002 - Busca com termo inexistente deve exibir mensagem de sem resultados")
    void buscaComTermoInexistenteDeveExibirMensagemAdequada() {
        homePage.abrirPagina();
        homePage.clicarIconeBusca();
        homePage.digitarTermoBusca("xyzxyzxyzconteudoinexistente123456");
        homePage.submeterBusca();

        assertTrue(homePage.mensagemSemResultadoExibida(),
            "Deve exibir mensagem informando que não há resultados para o termo pesquisado");

        assertFalse(homePage.resultadosExibidos(),
            "Não deve exibir artigos para um termo sem resultados");
    }

    // -------------------------------------------------------------------------
    // Cenário 3: Clicar em artigo dos resultados navega para página correta
    // -------------------------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("CT003 - Clicar em artigo do resultado deve navegar para a página do artigo")
    void clicarEmArtigoDeveNavegaParaPaginaDoArtigo() {
        homePage.abrirPagina();
        homePage.clicarIconeBusca();
        homePage.digitarTermoBusca("financas");
        homePage.submeterBusca();

        assertTrue(homePage.resultadosExibidos(),
            "Deve haver resultados para navegar");

        String urlEsperada = homePage.obterUrlPrimeiroResultado();
        homePage.clicarPrimeiroResultado();

        String urlAtual = homePage.obterUrlAtual();

        assertNotNull(urlAtual, "A URL após navegação não deve ser nula");
        assertNotEquals("https://blogdoagi.com.br/", urlAtual,
            "Deve ter navegado para fora da página de resultados");
        assertTrue(urlAtual.contains("blogdoagi.com.br"),
            "Deve permanecer no domínio do blog após clicar no artigo");
    }

    // -------------------------------------------------------------------------
    // Cenário 4 (bônus): Busca com campo vazio não deve quebrar a aplicação
    // -------------------------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("CT004 - Busca com campo vazio não deve causar erro na aplicação")
    void buscaComCampoVazioNaoDeveQuebrarAplicacao() {
        homePage.abrirPagina();
        homePage.clicarIconeBusca();
        homePage.digitarTermoBusca("");
        homePage.submeterBusca();

        String urlAtual = homePage.obterUrlAtual();
        assertNotNull(urlAtual, "A página deve continuar respondendo após busca vazia");
        assertTrue(homePage.paginaCarregada(),
            "O blog deve permanecer acessível após submeter busca vazia");
    }
}
