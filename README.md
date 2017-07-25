# BusDroid RJ
### Um simples aplicativo que permite localizar as linhas de ônibus disponíveis no município do Rio de Janeiro!

### Descrição:

###### Este é um simples aplicativo escrito em Java e feito para a plataforma Android API 14 (Android 4.0 em diante) que possui a função mostrar a localização das linhas de ônibus disponíveis no município do Rio de Janeiro, utilizando a API do data.rio para recuperar as informações dos ônibus em tempo real e utilizando a API do [Google Maps Android API](https://developers.google.com/maps/documentation/android-api/?hl=pt-br) para indicar o local exato dos ônibus em um mapa interativo.

###### A tela principal do aplicativo mostra dois possíveis modos de busca dos ônibus disponíveis no momento: O modo de busca pela linha do ônibus e pelo número de ordem do ônibus. 

###### Ao buscar pela linha, o aplicativo irá indicar todos os ônibus daquela linha que estão circulando. Ao buscar pelo número de ordem, o aplicativo irá indicar apenas o ônibus que possuir aquele número.

###### O aplicativo também poderá mostrar a localização atual do usuário no mapa, facilitando a identificação visual dos ônibus que estejam mais próximos ao usuário. Para habilitar esta função, é necessário conceder permissões de localização ao aplicativo e ligar a opção de Localização nas configurações do Android.
###### A função de localização é opcional, não sendo necessária para que o aplicativo funcione de maneira correta!

###### Ao buscar por uma linha de ônibus ou um ônibus específico através de seu número de ordem, uma janela com o Google Maps irá abrir e indicar através de marcadores as localizações dos veículos disponíveis no momento.

###### Os marcadores irão conter informações como a linha do ônibus, o número de ordem, a hora que o GPS do ônibus foi atualizado com o sistema e a velocidade do veículo naquele momento.

##### Abaixo está uma demonstração de busca por uma linha de ônibus específica:

<img src="http://i.imgur.com/TvKtSGF.png" width="100%" height="100%" />
<img src="http://i.imgur.com/QnkzcVG.png" width="100%" height="100%" />

## [Verifique o CHANGELOG.txt para maiores informações sobre novas versões!](https://raw.github.com/Wolfterro/BusDroid-RJ/master/CHANGELOG.txt)

### Idiomas Disponíveis:
 - Português Brasileiro (Padrão)
 - Inglês
 
 ### Permissões Necessárias:
 - Localização (Opcional)

### Download:

##### Para baixar o código-fonte do aplicativo, basta utilizar o git para clonar o repositório:
    git clone https://github.com/Wolfterro/BusDroid-RJ.git
    cd BusDroid-RJ/

##### Caso queira baixar o aplicativo já compilado, em formato .apk, use o link abaixo:
#### ***Release:*** https://github.com/Wolfterro/BusDroid-RJ/releases/tag/v1.1-Release

#### Atenção!
##### Caso queira compilar o aplicativo por conta própria, terá que obter uma chave da API do [Google Maps Android API](https://developers.google.com/maps/documentation/android-api/?hl=pt-br) vinculada a sua chave de assinatura do aplicativo! 
##### Caso contrário, o mapa do aplicativo NÃO IRÁ FUNCIONAR!

##### Caso não possua o git e queira baixar o repositório, baixe através deste [Link](https://github.com/Wolfterro/BusDroid-RJ/archive/master.zip) ou clique em "Clone or Download", no topo da página.
