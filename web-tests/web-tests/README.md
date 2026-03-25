# 🌐 Web Tests — Blog do Agi

Testes automatizados de UI para a funcionalidade de **pesquisa de artigos** do [Blog do Agi](https://blogdoagi.com.br/).

---

## 🛠️ Stack

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 11+ | Linguagem principal |
| Selenium WebDriver | 4.18.1 | Automação do browser |
| WebDriverManager | 5.8.0 | Gerenciamento automático do ChromeDriver |
| JUnit 5 | 5.10.2 | Framework de testes |
| Maven | 3.8+ | Gerenciamento de dependências e build |

---

## 📋 Cenários de Teste

| ID | Cenário | Tipo |
|---|---|---|
| CT001 | Busca com termo válido deve exibir artigos relacionados | Caminho feliz |
| CT002 | Busca com termo inexistente deve exibir mensagem adequada | Borda |
| CT003 | Clicar em artigo dos resultados deve navegar corretamente | Integração |
| CT004 | Busca com campo vazio não deve causar erro na aplicação | Borda/Negativo |

### Justificativa dos cenários

A **pesquisa de artigos** é a funcionalidade central analisada. Os cenários cobrem:
- O fluxo principal (usuário encontra o que busca)
- Tratamento de borda (termo sem resultado)
- Integridade de navegação (links funcionais)
- Robustez (entrada vazia não quebra o sistema)

---

## ⚙️ Pré-requisitos

- **Java 11+** — [Download](https://adoptium.net/)
- **Maven 3.8+** — [Download](https://maven.apache.org/download.cgi)
- **Google Chrome** instalado na máquina

> O `WebDriverManager` baixa e configura o `ChromeDriver` automaticamente. Não é necessário instalar manualmente.

---

## 🚀 Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git
cd SEU_REPOSITORIO/web-tests
```

### 2. Execute os testes

```bash
mvn test
```

### 3. Verifique o relatório

Após a execução, o relatório estará em:

```
target/surefire-reports/
```

Abra o arquivo `TEST-br.com.agi.tests.BlogSearchTest.xml` ou o `.txt` correspondente.

---

## 🔧 Execução em modo headless

Os testes já rodam em **modo headless** por padrão (sem abrir o browser visualmente), ideal para ambientes de CI/CD.

Para desabilitar o headless e ver o browser durante a execução local, edite o arquivo `DriverConfig.java` e remova a linha:

```java
options.addArguments("--headless");
```

---

## 🔄 Pipeline CI/CD (GitHub Actions)

O pipeline é ativado automaticamente em:
- Push para `main` ou `master`
- Pull Requests
- Execução manual via `workflow_dispatch`

O relatório de resultados é publicado diretamente na aba **Actions** do GitHub.

---

## 📁 Estrutura do Projeto

```
web-tests/
├── src/test/java/br/com/agi/
│   ├── config/
│   │   └── DriverConfig.java       # Configuração do WebDriver
│   ├── pages/
│   │   └── HomePage.java           # Page Object do blog
│   └── tests/
│       └── BlogSearchTest.java     # Casos de teste
├── .github/
│   └── workflows/
│       └── web-tests.yml           # Pipeline GitHub Actions
├── pom.xml
└── README.md
```

---

## 🏗️ Padrões utilizados

- **Page Object Model (POM):** separação entre lógica de teste e interação com a UI
- **Fluent Waits:** `WebDriverWait` com `ExpectedConditions` para evitar flakiness
- **Headless Chrome:** execução compatível com Linux/Windows/macOS e pipelines CI
- **JUnit 5 `@DisplayName`:** nomes descritivos nos relatórios de teste
