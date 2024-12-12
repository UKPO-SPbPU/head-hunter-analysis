Порядок запуска скриптов:
1. installer - инициализация docker
    a. mongodb работает на порте 27017
    b. mongo express работает на порте 8081
    c. elasticsearch работает на портах 9200 и 9300
    d. kibana работает на порте 5601
2. start_collector - сбор вакансий с head-hunter
3. start_parser - парсинг вакансий и фильтрация IT
4. start_elastic_downloader - загрузка вакансий в elasticsearch