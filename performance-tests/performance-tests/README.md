# ⚡ Performance Tests — BlazeDemo

Testes de performance para o fluxo de **compra de passagem aérea** no site [BlazeDemo](https://www.blazedemo.com), utilizando **Apache JMeter 5.6.3**.

---

## 🎯 Critério de Aceitação

| Métrica | Valor esperado |
|---|---|
| Throughput | ≥ 250 requisições/segundo |
| Tempo de resposta P90 | < 2.000 ms |

---

## 🧪 Tipos de Teste

### 🔵 Load Test (Teste de Carga)
Simula carga **sustentada** e crescente até atingir o critério definido.

| Configuração | Valor |
|---|---|
| Usuários simultâneos | 250 |
| Ramp-up | 60 segundos |
| Iterações por usuário | 10 |
| Think time entre passos | 1–2 segundos |

**Objetivo:** verificar se o sistema aguenta 250 req/s de forma estável.

---

### 🔴 Spike Test (Teste de Pico)
Simula um **pico abrupto** de carga — dobro da carga normal em poucos segundos.

| Configuração | Valor |
|---|---|
| Usuários simultâneos | 500 (2× o critério) |
| Ramp-up | 10 segundos (agressivo) |
| Iterações por usuário | 5 |

**Objetivo:** verificar como o sistema se comporta diante de uma sobrecarga repentina e se ele se recupera.

---

## 🛒 Cenário Testado

Fluxo completo de compra de passagem aérea:

```
GET  /                    → Página inicial
POST /reserve.php         → Seleção de voo (Boston → Rome)
POST /purchase.php        → Dados do comprador + cartão
GET  /confirmation.php    → Confirmação da compra
```

### Assertions configuradas
- Todos os passos: status HTTP 200
- Página inicial: contém texto `BlazeDemo`
- Reserva: contém texto `Choose your flight`
- Confirmação: contém texto `Thank you for your purchase`

---

## ⚙️ Pré-requisitos

- **Java 11+** — [Download](https://adoptium.net/)
- **Apache JMeter 5.6.3** — [Download](https://jmeter.apache.org/download_jmeter.cgi)

### Configurar JMeter no PATH (Linux/macOS)
```bash
export JMETER_HOME=/caminho/para/apache-jmeter-5.6.3
export PATH=$PATH:$JMETER_HOME/bin
```

### Configurar JMeter no PATH (Windows)
```
Painel de Controle → Sistema → Variáveis de Ambiente
Adicionar em PATH: C:\apache-jmeter-5.6.3\bin
```

---

## 🚀 Como executar

### Load Test
```bash
jmeter -n \
  -t performance-tests/scripts/blazedemo-load-test.jmx \
  -l performance-tests/reports/load-test-results.jtl \
  -e -o performance-tests/reports/load-test-html
```

### Spike Test
```bash
jmeter -n \
  -t performance-tests/scripts/blazedemo-spike-test.jmx \
  -l performance-tests/reports/spike-test-results.jtl \
  -e -o performance-tests/reports/spike-test-html
```

### Visualizar relatório HTML
Após a execução, abra no browser:
```
performance-tests/reports/load-test-html/index.html
performance-tests/reports/spike-test-html/index.html
```

> ⚠️ A pasta de output (`-o`) deve estar **vazia ou inexistente** antes de cada execução. Se necessário, apague antes: `rm -rf performance-tests/reports/load-test-html`

---

## 🔄 Pipeline CI/CD (GitHub Actions)

O pipeline pode ser executado manualmente via `workflow_dispatch`, escolhendo o tipo:
- `load` — apenas Load Test
- `spike` — apenas Spike Test
- `all` — ambos os testes

Em push para `main`, o Load Test é executado automaticamente. Os relatórios HTML ficam disponíveis como **Artifacts** na aba Actions por 30 dias.

---

## 📁 Estrutura do Projeto

```
performance-tests/
├── scripts/
│   ├── blazedemo-load-test.jmx     # Plano de carga
│   └── blazedemo-spike-test.jmx    # Plano de pico
├── reports/                        # Gerado após execução
│   ├── load-test-results.jtl
│   ├── load-test-html/
│   ├── spike-test-results.jtl
│   └── spike-test-html/
├── .github/
│   └── workflows/
│       └── performance-tests.yml
└── README.md
```

---

## 📊 Relatório de Execução e Análise

> **Nota:** os valores abaixo são referências esperadas com base nas características do ambiente BlazeDemo (servidor de demonstração compartilhado). Execute os testes e substitua pelos resultados reais.

### Load Test — Resultados Esperados

| Passo | Amostras | Tempo Médio | P90 | Erro % |
|---|---|---|---|---|
| GET / | 250× | ~300 ms | ~600 ms | 0% |
| POST /reserve.php | 250× | ~500 ms | ~900 ms | 0% |
| POST /purchase.php | 250× | ~600 ms | ~1100 ms | 0% |
| GET /confirmation.php | 250× | ~400 ms | ~800 ms | 0% |

### Spike Test — Resultados Esperados

| Passo | Amostras | Tempo Médio | P90 | Erro % |
|---|---|---|---|---|
| GET / | 500× | ~600 ms | ~1500 ms | < 5% |
| POST /reserve.php | 500× | ~800 ms | ~1800 ms | < 5% |
| POST /purchase.php | 500× | ~900 ms | ~1900 ms | < 5% |
| GET /confirmation.php | 500× | ~700 ms | ~1600 ms | < 5% |

---

## ✅ Análise do Critério de Aceitação

### Load Test
**Critério SATISFEITO** ✅ (projeção)

- O BlazeDemo é um ambiente de demonstração leve, sem lógica de negócio pesada.
- Com 250 usuários e ramp-up de 60s, o throughput tende a superar 250 req/s.
- O P90 projetado fica bem abaixo de 2.000 ms em condições normais de rede.

### Spike Test
**Critério PARCIALMENTE SATISFEITO** ⚠️ (projeção)

- Com 500 usuários em ramp-up de 10s, o servidor pode apresentar degradação.
- É esperado que o P90 se aproxime ou ultrapasse o limite de 2s sob pico extremo.
- Isso **não é falha do teste** — o objetivo do Spike Test é justamente medir o comportamento sob sobrecarga, não necessariamente passar no critério de aceitação normal.
- O critério de 250 req/s e P90 < 2s aplica-se ao **Load Test**; o Spike Test serve para avaliar resiliência e recuperação.

### Conclusão Geral
O critério de aceitação **é atendido pelo Load Test**. O Spike Test revela os limites do sistema e serve como insumo para decisões de escalabilidade (ex: autoscaling, CDN, otimização de queries).

> ⚠️ **Importante:** o BlazeDemo é um servidor público compartilhado. Os resultados reais podem variar dependendo da carga de outros usuários no momento da execução. Execute o teste em horários de baixo tráfego para resultados mais estáveis.
