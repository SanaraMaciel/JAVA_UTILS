CPqD PCRI - ENDEREÇO - VIACEP
=

**Versão:** 1.0<br />
**Nivel do documento:** Intermediário<br />
**Tecnologias utilizadas:** Java, Spring, Apache Camel (HTTP4, Rest, Swagger, Jackson)<br />
<br />
<br />


Configurando a aplicação de integração com o VIACEP para execução
-

A execução da aplicação de integração ocorre via console do servidor e é basicamente a execução de um JAR (Java Archive) através de um terminal de comando e por meio de uma série de parâmetros que devem ser configurados conforme listados a seguir.

```
java -jar -DAPP_LOG_ROOT=/opt/pcri/viacep/logs -DINTEGRATION_DB_HOME=/opt/pcri -Dserver.port=8086 -Xms512m -Xmx2048m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -Djava.net.preferIPv4Stack=true -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -XX:+UseG1GC -Duser.Timezone=GMT-3 -Duser.language=pt -Duser.country=BR -Xrunjdwp:transport=dt_socket,address=8792,server=y,suspend=n Integration-viacep-camel.jar &
```

Como pode ser notado no exemplo acima será necessária a passagem de parâmetros de várias informações para a aplicação. São elas:
* **APP_LOG_ROOT** - Localização do diretório raiz onde os  _logs_  da aplicação serão gerados.
* **server.port** - Porta a qual o servidor será configurado para acesso, inclusive de  _entrypoints_  disponíveis na aplicação.
* **-Xms512m -Xmx2048m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -Djava.net.preferIPv4Stack=true** - Configuração de uso de memória.
* **-XX:+UseStringDeduplication -XX:+OptimizeStringConcat** - Configuração de otimização de uso  _string_  na aplicação.
* **-XX:+UseG1GC** - Configuração do uso do  _Garbage Collector_ .
* **-Duser.Timezone=GMT-3 -Duser.language=pt -Duser.country=BR** - Configuração do  _locale_  da aplicação.
* **-Xrunjdwp:transport=dt_socket,address=8793,server=y,suspend=n** - Configuração para ser possível realizar  _debug_  da aplicação através da  _IDE_ .

**Os valores associados a cada variável poderá ser alterado de acordo com o ambiente de implantação. Basicamente é um apoio à configuração de execução da aplicação e serve apenas como sugestão.**
<br />
<br />
<br />

Acessando a documentação Swagger para as API's do componente
-

O componente de integração com o VIACEP disponibiliza um ou mais  _entrypoints_  para o uso junto ao fluxo principal da aplicação. Para que o desenvolvedor possa conhecê-la e utilizá-la existe uma documentação desenvolvida no  _Swagger_  que é disponibilizada no momento ao qual a aplicação fica no ar pronta para acesso. Uma  _URL_  específica deve ser acessada para visualização da documentação conforme mostrado abaixo:

```
http://<host>:<porta>/pcri/viacep/swagger/api-docs
```

Como pode ser notado no exemplo acima será necessário alterar algumas informações para acessar a documentação. São elas:
* **host** - Nome ou IP do servidor onde a aplicação de integração foi inicializada.
* **porta** - Porta de acesso à aplicação. É a mesma que foi configurada na seção anterior no momento da subida da aplicação de integração.

Ao acessar a  _URL_  acima será disponibilizado o  _json_  da documentação. Para uma leitura mais fácil recomenda-se copiar o texto e transportá-lo para o editor  _Swagger online_  que pode ser acessado em:

```
https://editor.swagger.io/
```

Através do editor  _Swagger_  é possível inclusive executar os serviços disponibilizados.
<br />
<br />
<br />