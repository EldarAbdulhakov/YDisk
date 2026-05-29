# 1. Создание папки (PUT /v1/disk/resources)

## TC-1.1 Создание папки в корневом каталоге возвращает 201

Предусловие:
1. Авторизоваться в Яндекс ID
2. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
3. Получить токен, нажав на кнопку “Получить OAuth-токен”
4. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token
5. Папка отсутствует в корневом каталоге на диске

Шаги:
1. Отправить PUT запрос по адресу:  
https://cloud-api.yandex.net/v1/disk/resources?path=folderName

Ожидаемый результат:
1. Статус код 201
2. JSON-ответ body:

       {
          "method": "GET",
          "href": "https://cloud-api.yandex.net/v1/disk/resources?path=disk%3A%2FfolderName",
          "templated": false
       }

## TC-1.2 Создание вложенной папки возвращает 201

Предусловие:
1. Создать папку folderName (см. ТС-1.1)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить PUT запрос по адресу:  
https://cloud-api.yandex.net/v1/disk/resources?path=folderName/nestedFolder

Ожидаемый результат:
1. Статус код 201
2. JSON-ответ body:
        
       {
          "method": "GET",
          "href": "https://cloud-api.yandex.net/v1/disk/resources?path=disk%3A%2FfolderName%2FnestedFolder",
          "templated": false
       }

## TC-1.3 Создание папки с существующим именем возвращает 409
Предусловие:
1. Создать папку folderName (см. ТС-1.1)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить PUT запрос по адресу:  
https://cloud-api.yandex.net/v1/disk/resources?path=folderName

Ожидаемый результат:
1. Статус код 409
2. JSON-ответ body:

       {
          "error": "DiskPathPointsToExistentDirectoryError",
          "description": "Specified path \"folderName\" points to existent directory.",
          "message": "По указанному пути \"folderName\" уже существует папка с таким именем."
       }

## TC-1.4 Создание папки без OAuth токена возвращает 401

Шаги:
1. Отправить PUT запрос без OAuth токена по адресу:    
https://cloud-api.yandex.net/v1/disk/resources?path=folderName

Ожидаемый результат:
1. Статус код 401
2. JSON-ответ body:

       {
          "error": "UnauthorizedError",
          "description": "Unauthorized",
          "message": "Не авторизован."
       }


# 2. Удаление папки (DELETE /v1/disk/resources)

## TC-2.1 Удаление пустой папки в корзину возвращает 204

Предусловие:
1. Создать папку folderName (см. ТС-1.1)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить DELETE запрос по адресу:  
https://cloud-api.yandex.net/v1/disk/resources?path=folderName

Ожидаемый результат:
1. Статус код 204

## TC-2.2 Безвозвратное удаление пустой папки возвращает 204

Предусловие:
1. Создать папку folderName (см. ТС-1.1)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить DELETE запрос по адресу:  
https://cloud-api.yandex.net/v1/disk/resources?path=folderName&permanently=true

Ожидаемый результат:
1. Статус код 204

## TC-2.3 Удаление папки с вложенной папкой в корзину возвращает 202 

Предусловие:
1. Создать вложенную папку (см. ТС-1.2)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить DELETE запрос по адресу:   
https://cloud-api.yandex.net/v1/disk/resources?path=folderName

Ожидаемый результат:
1. Статус код 202
2. JSON-ответ body:

       {
          "method": "GET",
          "href": "https://cloud-api.yandex.net/v1/disk/operations/1a0bd7857bbca8be72ce560ad57ddcb275ededca96276bd76326277f02433a8c",
          "templated": false
       }

## TC-2.4 Удаление несуществующей папки в корзину возвращает 404

Предусловие:
1. Авторизоваться в Яндекс ID
2. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
3. Получить токен, нажав на кнопку “Получить OAuth-токен”
4. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Убедиться, что папки folderName не существует
2. Отправить DELETE запрос по адресу:  
https://cloud-api.yandex.net/v1/disk/resources?path=folderName

Ожидаемый результат:
1. Статус код 404
2. JSON-ответ body:

       {
          "error": "DiskNotFoundError",
          "description": "Resource not found.",
          "message": "Не удалось найти запрошенный ресурс."
       }

## TC-2.5 Удаление папки без токена авторизации возвращает 401

Предусловие:
1. Создать папку folderName (см. ТС-1.1)

Шаги:
1. Отправить DELETE запрос без OAuth токена по адресу:  
https://cloud-api.yandex.net/v1/disk/resources?path=folderName

Ожидаемый результат:
1. Статус код 401
2. JSON-ответ body:

       {
           "error": "UnauthorizedError",
           "description": "Unauthorized",
           "message": "Не авторизован."
       }


# 3. Восстановление папки из корзины (PUT /v1/disk/trash/resources/restore)

##TC-3.1 Восстановление пустой папки из корзины возвращает 201

Предусловие:
1. Удалить  папку folderName в корзину (см. ТС-2.1)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить GET запрос на получение данных из корзины по адресу:
https://cloud-api.yandex.net/v1/disk/trash/resources  
JSON-ответ body (пример):

       {
          "path": "trash:/",
          "type": "dir",
          "name": "trash",
          "created": "2012-04-04T20:00:00+00:00",
          "modified": "2012-04-04T20:00:00+00:00",
          "_embedded": {
             "path": "trash:/",
             "limit": 20,
             offset": 0,
             "sort": "",
             "total": 1,
             "items": [
                {
                   "path": "trash:/folderName_49a0555683a24276d7438d884b73ae0cbd53578e",
                    "type": "dir",
                    "name": "folderName",
                    "created": "2026-05-28T11:56:04+00:00",
                    "modified": "2026-05-28T12:01:35+00:00",
                    "resource_id": "287807606:c99335ad9e6d0e12f15ed86f645056471564b998aa2d93c011a4e244ac8cba51",
                    "revision": 1769602070649270,
                    "comment_ids": {
                    "public_resource": "287807606:c99335ad9e6d0e12f15ed86f645056471564b998aa2d93c011a4e244ac8cba51",
                    "private_resource": "287807606:c99335ad9e6d0e12f15ed86f645056471564b998aa2d93c011a4e244ac8cba51"
                    },
                    "exif": {},
                    "origin_path": "disk:/folderName",
                    "deleted": "2026-05-28T12:07:50+00:00"
                }
             ]
          },
          "resource_id": "287807606:ba11d9431bd2cc46c43ecc07c9f597e67940489ca1a78cd82a19778848491472",
          "revision": 1769597674485686,
          "comment_ids": {},
          "exif": {},
          "origin_path": null
       }

2. Найти и скопировать в теле ответа значение поля “path”, в котором присутствует имя папки folderName
3. Отправить PUT запрос по адресу: 
https://cloud-api.yandex.net/v1/disk/trash/resources/restore?path={folderPath},   
где folderPath = trash:/folderName_49a0555683a24276d7438d884b73ae0cbd53578e - “path” из пункта 2

Ожидаемый результат:
1. Статус код 201
2. JSON-ответ body:

       {
          "method": "GET",
          "href": "https://cloud-api.yandex.net/v1/disk/resources?path=disk%3A%2FfolderName",
          "templated": false
       }

## TC-3.2 Восстановление папки с вложенной папкой из корзины возвращает 202

Предусловие:
1. Удалить  папку folderName с вложением в корзину (см. ТС-2.3)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить GET запрос на получение данных из корзины по адресу:
https://cloud-api.yandex.net/v1/disk/trash/resources  
JSON-ответ body (пример):

       {
          "path": "trash:/",
          "type": "dir",
          "name": "trash",
          "created": "2012-04-04T20:00:00+00:00",
          "modified": "2012-04-04T20:00:00+00:00",
          "_embedded": {
             "path": "trash:/",
             "limit": 20,
             "offset": 0,
             "sort": "",
             "total": 1,
             "items": [
                {
                   "path": "trash:/folderName_49a0555683a24276d7438d884b73ae0cbd53578e",
                   "type": "dir",
                   "name": "folderName",
                   "created": "2026-05-28T11:56:04+00:00",
                   "modified": "2026-05-28T12:01:35+00:00",
                   "resource_id": "287807606:c99335ad9e6d0e12f15ed86f645056471564b998aa2d93c011a4e244ac8cba51",
                   "revision": 1769602070649270,
                   "comment_ids": {
                   "public_resource": "287807606:c99335ad9e6d0e12f15ed86f645056471564b998aa2d93c011a4e244ac8cba51",
                   "private_resource": "287807606:c99335ad9e6d0e12f15ed86f645056471564b998aa2d93c011a4e244ac8cba51"
                   },
                   "exif": {},
                   "origin_path": "disk:/folderName",
                   "deleted": "2026-05-28T12:07:50+00:00"
                }
             ]
          },
          "resource_id": "287807606:ba11d9431bd2cc46c43ecc07c9f597e67940489ca1a78cd82a19778848491472",
          "revision": 1769597674485686,
          "comment_ids": {},
          "exif": {},
          "origin_path": null
       }
2. Найти и скопировать в теле ответа значение поля “path”, в котором присутствует имя папки folderName
3. Отправить PUT запрос по адресу:  
https://cloud-api.yandex.net/v1/disk/trash/resources/restore?path={folderPath},   
где folderPath = trash:/folderName_49a0555683a24276d7438d884b73ae0cbd53578e - “path” из пункта 2

Ожидаемый результат:
1. Статус код 202
2. JSON-ответ body:

       {
          "method": "GET",
          "href": "https://cloud-api.yandex.net/v1/disk/operations/2649c8ddf548eece6804045fd9e9250b4e9a21255c77b3a2187299e73fd720b1",
          "templated": false
       }

## TC-3.3 Восстановление папки из корзины без токена авторизации возвращает 401

Предусловие:
1. Удалить  папку folderName в корзину (см. ТС-2.1)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить GET запрос на получение данных из корзины по адресу:    
https://cloud-api.yandex.net/v1/disk/trash/resources
JSON-ответ body:

       {
          "path": "trash:/",
          "type": "dir",
          "name": "trash",
          "created": "2012-04-04T20:00:00+00:00",
          "modified": "2012-04-04T20:00:00+00:00",
          "_embedded": {
             "path": "trash:/",
             "limit": 20,
             "offset": 0,
             "sort": "",
             "total": 1,
             "items": [
                {
                   "path": "trash:/folderName_49a0555683a24276d7438d884b73ae0cbd53578e",
                   "type": "dir",
                   "name": "folderName",
                   "created": "2026-01-28T11:56:04+00:00",
                   "modified": "2026-01-28T12:01:35+00:00",
                   "resource_id": "287807606:c99335ad9e6d0e12f15ed86f645056471564b998aa2d93c011a4e244ac8cba51",
                   "revision": 1769602070649270,
                   "comment_ids": {
                   "public_resource": "287807606:c99335ad9e6d0e12f15ed86f645056471564b998aa2d93c011a4e244ac8cba51",
                   "private_resource": "287807606:c99335ad9e6d0e12f15ed86f645056471564b998aa2d93c011a4e244ac8cba51"
                   },
                   "exif": {},
                   "origin_path": "disk:/folderName",
                   "deleted": "2026-01-28T12:07:50+00:00"
                }
             ]
          },
          "resource_id": "287807606:ba11d9431bd2cc46c43ecc07c9f597e67940489ca1a78cd82a19778848491472",
          "revision": 1769597674485686,
          "comment_ids": {},
          "exif": {},
          "origin_path": null
       }
2. Найти и скопировать в теле ответа значение поля “path”, в котором присутствует имя папки folderName
3. Отправить PUT запрос без токена авторизации по адресу:  
https://cloud-api.yandex.net/v1/disk/trash/resources/restore?path={folderPath},   
где folderPath = trash:/folderName_49a0555683a24276d7438d884b73ae0cbd53578e - “path” из пункта 2

Ожидаемый результат:
1. Статус код 401
2. JSON-ответ body:
       
       {
          "error": "UnauthorizedError",
          "description": "Unauthorized",
          "message": "Не авторизован."
       }

## TC-3.4 Восстановление несуществующей папки из корзины возвращает 404

Предусловие:
1. Авторизоваться в Яндекс ID
2. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
3. Получить токен, нажав на кнопку “Получить OAuth-токен”
4. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить PUT запрос с несуществующим ресурсом по адресу:  
https://cloud-api.yandex.net/v1/disk/trash/resources/restore?path=notExistfolderName

Ожидаемый результат:
1. Статус код 404
2. JSON-ответ body:
       
       {
          "error": "DiskNotFoundError",
          "description": "Resource not found.",
          "message": "Не удалось найти запрошенный ресурс."
       }


# 4. Создание текстового файла (PUT href)

## TC-4.1 Создание текстового файла в корневой директории возвращает 201

Предусловие:
1. Авторизоваться в Яндекс ID
2. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
3. Получить токен, нажав на кнопку “Получить OAuth-токен”
4. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить GET запрос для получение ссылки "href" для загрузки файла по адресу:  
https://cloud-api.yandex.net/v1/disk/resources/upload?path=data.txt  
JSON-ответ body:
       
       {
          "method": "PUT",
          "href": "https://uploader23klg.disk.yandex.net:443/upload-target/20260129T003203.012.utd.6pf838xwxxkuta4jk560iujj4-k23klg.3683810",
          "templated": false,
          "operation_id": "44103b0715bb99324ab22a98e228c3447b0d586cd9f6cf3723e043901b2c59a4"
       }
2. Отправить PUT запрос с полученным href (PUT<href>) по адресу:  
https://uploader23klg.disk.yandex.net:443/upload-target/20260129T003203.012.utd.6pf838xwxxkuta4jk560iujj4-k23klg.3683810
В body включить файл data.txt

Ожидаемый результат:
1. Статус код 201

## TC-4.2 Создание текстового файла в папке возвращает 201

Предусловие:
1. Создать папку folderName (см. ТС-1.1)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить GET запрос для получение ссылки "href" для загрузки файла по адресу:  
https://cloud-api.yandex.net/v1/disk/resources/upload?path=folderName/data.txt   
JSON-ответ body:  

       {
          "method": "PUT",
          "href": "https://uploader23klg.disk.yandex.net:443/upload-target/20260129T003203.012.utd.6pf838xwxxkuta4jk560iujj4-k23klg.3683810",
          "templated": false,
          "operation_id": "44103b0715bb99324ab22a98e228c3447b0d586cd9f6cf3723e043901b2c59a4"
       }
2. Отправить PUT запрос с полученным href (PUT<href>) по адресу:   
https://uploader23klg.disk.yandex.net:443/upload-target/20260129T003203.012.utd.6pf838xwxxkuta4jk560iujj4-k23klg.3683810
В body включить файл data.txt

Ожидаемый результат:
1. Статус код 201

## TC-4.3 Создание текстового файла без токена авторизации возвращает 401

Шаги:
1. Отправить GET запрос для получение ссылки "href" для загрузки файла по адресу:  
https://cloud-api.yandex.net/v1/disk/resources/upload?path=data.txt
2. Отправить PUT запрос с полученным href (PUT<href>) по адресу:  
https://uploader23klg.disk.yandex.net:443/upload-target/20260129T003203.012.utd.6pf838xwxxkuta4jk560iujj4-k23klg.3683810   
В body включить файл data.txt  

Ожидаемый результат:
1. Статус код 401
2. JSON-ответ body:

       {
          "error": "UnauthorizedError",
          "description": "Unauthorized",
          "message": "Не авторизован."
       }


# 5. Копирование файла (POST v1/disk/resources/copy)

## TC-5.1 Копирование текстового файла между папками возвращает 201

Предусловие:
1. Создать папку input_data (см. ТС-1.1)
2. Создать папку output_data (см. ТС-1.1)
3. Авторизоваться в Яндекс ID
4. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
5. Получить токен, нажав на кнопку “Получить OAuth-токен”
6. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить GET запрос для получение ссылки "href" по адресу:  
https://cloud-api.yandex.net/v1/disk/resources/upload?path=input_data/data.txt  
   JSON-ответ body:

       {
          "method": "PUT",
          "href": "https://uploader23klg.disk.yandex.net:443/upload-target/20260129T003203.012.utd.6pf838xwxxkuta4jk560iujj4-k23klg.3683810",
          "templated": false,
          "operation_id": "44103b0715bb99324ab22a98e228c3447b0d586cd9f6cf3723e043901b2c59a4"
       }  

2. Отправить PUT запрос с полученным href (PUT<href>) для загрузки файла в папку input_data по адресу:   
   https://uploader23klg.disk.yandex.net:443/upload-target/20260129T003203.012.utd.6pf838xwxxkuta4jk560iujj4-k23klg.3683810
   В body включить файл data.txt
3. Отправить POST запрос, с параметрами from=input_data/data.txt и path=output_data/data.txt по адресу:  
   https://cloud-api.yandex.net/v1/disk/resources/copy

Ожидаемый результат:  
1. Статус код 201
2. JSON-ответ body:

       {
          "method": (string): <HTTP-метод>,
          "href": "https://cloud-api.yandex.net/v1/disk/resources?path=disk%3A%2Foutput_data%2Fdata.txt"
       }  

## TC-5.2 Копирование текстового файла в папку, содержащую файл с тем же именем, возвращает 409

Предусловие:
1. Копировать текстовый файл между папками(см. ТС-5.1)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token

Шаги:
1. Отправить POST запрос, с параметрами from=input_data/data.txt и path=output_data/data.txt по адресу:  
   https://cloud-api.yandex.net/v1/disk/resources/copy 

Ожидаемый результат:  
1. Статус код 409  
2. JSON-ответ body:  

       {
          "error": "DiskResourceAlreadyExistsError",
          "description": "Resource \"output_data/data.txt\" already exists.",
          "message": "Ресурс \"output_data/data.txt\" уже существует." 
       }  

# 6. Скачивание текстового файла возвращает 200 (GET)

## TC-6.1 Скачивание текстового файла возвращает 200

Предусловие:
1. Создать папку folderName (см. ТС-1.1)
2. Авторизоваться в Яндекс ID
3. Перейти в Полигон по ссылке: https://yandex.ru/dev/disk/poligon/
4. Получить токен, нажав на кнопку “Получить OAuth-токен”
5. В заголовке (Headers) добавить OAuth-токен: Authorization: OAuth auth_token
6. Сгенерировать текстовый файл data.txt с данными:  
          
       username=Petr
       password=secret_key

Шаги:
1. Отправить GET запрос для получение ссылки "href" по адресу:  
   https://cloud-api.yandex.net/v1/disk/resources/upload?path=folderName/data.txt  
   JSON-ответ body:

       {
          "method": "PUT",
          "href": "https://uploader23klg.disk.yandex.net:443/upload-target/20260129T003203.012.utd.6pf838xwxxkuta4jk560iujj4-k23klg.3683810",
          "templated": false,
          "operation_id": "44103b0715bb99324ab22a98e228c3447b0d586cd9f6cf3723e043901b2c59a4"
       }  
2. Отправить PUT запрос с полученным href (PUT<href>) для загрузки файла в папку folderName:   
   https://uploader23klg.disk.yandex.net:443/upload-target/20260129T003203.012.utd.6pf838xwxxkuta4jk560iujj4-k23klg.3683810  
В body включить файл data.txt
3. Получить ссылку "href" на скачивание файла data.txt GET запросом по адресу:  
   https://cloud-api.yandex.net/v1/disk/resources/download  
   В запросе использовать параметр: path=folderName/data.txt  
   JSON-ответ body:  

       {
          "method": "GET",
          "href": "https://downloader.disk.yandex.ru/disk/f185090229890f6ed65e03bc5a61dcda338a6666e62ee37b89f95aed6de16632/6a19a03c/K5aAbuWDjKwWMEYCHqVBYalNnYyNMvIjv8elByRelNP9vwpm7f1eeqhN5nVzC07EncM-fm4L5ZF8ZsoS5o1_qg%3D%3D?uid=287807606&filename=data.txt&disposition=attachment&hash=&limit=0&content_type=text%2Fplain&owner_uid=287807606&fsize=34&hid=8eeea5b4625547efb2847de7835dc81f&media_type=document&tknv=v3&is_direct_zip_experiment=1&etag=155c21c3766c64acc55f855effb0b409",
          "templated": false
       }

4. Скачать и прочитать файл data.txt GET запросом по адресу, полученным в пункте 3:
   https://downloader.disk.yandex.ru/disk/f185090229890f6ed65e03bc5a61dcda338a6666e62ee37b89f95aed6de16632/6a19a03c/K5aAbuWDjKwWMEYCHqVBYalNnYyNMvIjv8elByRelNP9vwpm7f1eeqhN5nVzC07EncM-fm4L5ZF8ZsoS5o1_qg%3D%3D?uid=287807606&filename=data.txt&disposition=attachment&hash=&limit=0&content_type=text%2Fplain&owner_uid=287807606&fsize=34&hid=8eeea5b4625547efb2847de7835dc81f&media_type=document&tknv=v3&is_direct_zip_experiment=1&etag=155c21c3766c64acc55f855effb0b409

Ожидаемый результат:
1. Статус код 200
2. Ответ в body содержит данные файла data.txt:  
   
       username=Petr
       password=secret_key
