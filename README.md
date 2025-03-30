**Разработка мобильного приложения для каталогизации книг с помощью ИИ**

### Термины и определения

- *Система каталогизации библиотечных фондов* — система типа «клиент–сервер», обеспечивающая внесение, хранение, модификацию данных о составе и состоянии библиотечного фонда организации.
- *Аутентификация* и *авторизация*. Аутентификация проверяет личность пользователя по его запросу к системе. Авторизация выполняется системой для разрешения на доступ к определенным ресурсам и действия с ними.
- *Роль пользователя*  — совокупность разрешений (прав), предоставляемых пользователю системой. 
- *Единица (экземпляр) библиотечного хранения* — физический экземпляр публикации, рукописи, диссертации, ящика с картотекой и т.п., хранящийся в библиотечных отделах. Каждая единица получает при каталогизации свой уникальный номер. 
  ⚠️ Одна и та же публикация (книга, выпуск журнала, препринт, карта и т.п.) может быть представлена несколькими единицами хранения, т.е. храниться в нескольких экземплярах, все они получают одно и то же библиографическое описание, но разные идентификаторы. 
- *Библиографическое описание* — описание единицы хранения, выполненное в соответствии с выбранной системой (MARC, BibTex и т.п.); хранится в виде структурированных записей в базе данных системы.
- *Идентификатор* единицы библиотечного хранения — уникальная для всего хранения символьная последовательность, присвоенная экземпляру для его однозначной идентификации. Может быть присвоена каталогизатором вручную либо сгенерирована автоматически, в том числе случайным образом.
- *Каталогизатор* — роль, наделенная правами внесения, исправления, удаления записей библиографического описания, присвоения единицам хранения новых идентификаторов и нанесения маркировки.
- *Маркировка* единиц хранения — этикетка или наклейка, которая наносится на поверхность экземпляра хранения. 
  ⚠️ Если Экземпляр состоит из нескольких частей (книга в футляре или коробке, книга с вложением и т.п.), то идентичная маркировка наносится на все части. В каталоге фиксируется состав сложного экземпляра хранения.
- *Администратор* — роль, дающая право назначать исполнителей на роль каталогизаторов

### Размещение куаркодов и их назначение
Куаркоды размещаются: 
- на каждом экземпляре хранения и содержат URL адрес с библиографическом описанием этого экземпляра в базе данных.
- на полке книжного шкафа размещается куаркод с адресом веб-страницы, описывающем все книги этой полки.
- На жетоне Лаборанта и Администратора и содержат URL адрес страницы авторизации, см. ниже.

### Роли пользователей системы и схема авторизации:
- **Администратор** регистрируется по логину и паролю. Помимо прочего администратор определяет список Лаборантов, имеющих доступ к системе, и их права на работу с библиографической базой данных.
* **Лаборанту** предпочтительно реализовать доступ к системе без регистрации по логину и паролю, например, по следующей схеме: лаборант получает на время работы личный куаркод, служащий для аутентификации. При переходе по ссылке с куаркода сервер на основе данных об устройстве, с которого зашел лаборант, создает токен рабочего сеанса, который наделяет лаборанта правами данной роли на один сеанс работы – ограниченный срок в заданное администратором рабочее время. Авторизация лаборанта по паролю не нужна. 
- **Читатель** получает доступ к электронному библиографическому каталогу по опубликованному открытому адресу без аутентификации и авторизации, может просматривать каталог, выполнять поиск, получать детальное библиографическое описание на нужную ему _единицу библиотечного хранения_ и _физический адрес ее хранения_ в организации.

  
### Как мы видим работу приложения

#### 1\. Регистрация и вход для сотрудников

* Регистрация:  
  * Сотрудники должны зарегистрироваться, указав имя, электронную почту и пароль.  
  * ?подтверждение через почту  
  * При регистрации создается уникальная учетная запись в базе данных.  
* Вход:  
  * При входе сотрудник вводит свои учетные данные (электронная почта и пароль).  
  * После успешной аутентификации сотрудник попадает на главную страницу приложения.

---

#### 2\. Сканирование книги

* Процесс:  
  * Сотрудник нажимает кнопку "Сканировать книгу".  
  * Включается камера устройства, и пользователь фотографирует обложку, титульный лист, техническую страницу книги с выходными данными.  
  * Приложение обрабатывает изображение с помощью модели ИИ.
  * Если на обложке книги уже есть библиотечный  куаркод, то он считывается в первую очередь и по нему открывается страница с описанием и сведениями о физическом месте ее хранения. Информация необходима для возврета книги на место ее хранения.
  * Из изображения извлекаются ключевые данные: ISBN, название книги, автор, год и место издания, издательство, имена редакторов и прочая информация. ⚠️Преобразование изображения в текст (text-to-image) может происходить с ошибками: пропусками частей текста, ошибочным распознаванием отдельных символом или слов, вставками лишнего текста; полученная информация должна в дельнейшем сопоставляться с эталонной библиографической базой. 
* Обработка:  
  * После извлечения данных приложение автоматически заполняет соответствующие поля формы для добавления книги. Если книга с подобным библиографическим описанием находится уже в каталоге (размещена в другом месте хранения, и перед нами ее другой экземпляр), то открывается страница с имеющимся описанием.
  * Если данные извлечены неправильно, сотрудник может их отредактировать вручную (задача системы -- минимизировать этот труд, а в идиале вообще исключить ручной ввод для большинства книг).

---

#### 3\. Добавление книги в базу данных

* После сканирования сотрудник:  
  * Проверяет и подтверждает данные о книге.  
  * Указывает местоположение книги (например, секция, полка, ячейка). ⚠️ Описание всей конфигурации системы фихицеского хранения задается администратором: это почтовый адрес организации, название подразделения, номер этажа, номер кабинета, номер шкафа (стелажа, сейфа), номер полки.
  * Приложение сохраняет всю информацию о книге в базе данных.

---

#### 4\. Формирование маркировки

* Процесс:  
  * Для каждой книги приложение генерирует уникальную маркировку (QR-код или штрихкод). 
  * Маркировка содержит ID книги, её местоположение и другую важную информацию.  
* Печать:  
  * Приложение предоставляет функцию экспорта маркировки в PDF и отправки её на принтер.  
  * Распечатанная маркировка приклеивается на книгу.

---

#### 5\. Поиск книги

* По названию, автору или другому параметру: сотрудник вводит запрос в строку поиска, и приложение выводит все совпадения.  
* Результаты:  
  * Приложение отображает карточку книги с детальной информацией и указанием места хранения.

---

#### 6\. Управление статусом книги

* Выдача книги:  
  * При выдаче книги сотрудник сканирует её QR-код.  
  * Приложение фиксирует, что книга выдана, и сохраняет информацию о сотруднике, который взял её.  
* Возврат книги:  
  * При возврате сотрудник сканирует маркировку, и статус книги обновляется на "в наличии".  
* Учет передвижений:  
  * Приложение хранит историю перемещений каждой книги (где она находилась, кто её брал и когда).

**Задачи:**

**Кира:** 

**Проектирование БД:**

* Анализ структуры данных:  
  * Определить, какие данные необходимо хранить (название книги, автор, жанр, местоположение, уникальный идентификатор, история перемещений и т.д.).  
  * Разработать таблицы, включая связи между ними (например, таблица книг, таблица пользователей, таблица местоположений).  
* Составление ER-диаграммы  
* Определение требований к базе данных

**Формирование маркировки:**

**Дизайн маркировки:**

* Определить формат маркировки (QR-код, штрихкод или уникальный текстовый идентификатор).  
* Указать, какие данные будут закодированы (уникальный ID книги, её местоположение и другая необходимая информация).  
* Как будет выглядеть маркировка, что будет изображено.

**Интеграция генератора маркировки:**

* Исследовать библиотеки для генерации QR-кодов или штрихкодов (например, Zxing, QR Code Generator).  
* Написать функцию, которая автоматически создаёт маркировку для каждой новой книги.

**Подготовка для печати:**

* Создать шаблон для маркировки, который можно легко распечатать и наклеить на книгу.

**Фронтенд:**

**Разработка интерфейса:**

* Создать дизайн основных экранов приложения: основной экран, экран сканирования книги, экран добавления книги, экран поиска и просмотра информации, экран регистрации и входа.

**Тестирование интерфейса.**

**Диана:** 

**Разработка БД:**

* **Создание структуры базы данных:**  
  * Реализовать таблицы в выбранной СУБД.  
  * Настроить первичные и внешние ключи для связи таблиц.  
* **Настройка сервера базы данных:**  
  * Установить и настроить сервер базы данных для локальной и удалённой работы.  
* **Реализация CRUD-операций:**  
  * Создать функции для добавления, удаления, редактирования и поиска данных в базе.  
* **Оптимизация производительности:**  
  * Настроить индексы для ускорения запросов.  
  * Обеспечить резервное копирование данных.

**Компьютерное зрение:**

**Обучение и интеграция модели ИИ:**

* Найти подходящую модель для извлечения текста с изображений (например, модели из Hugging Face, Google Vision API) или создать/дообучить.  
* Настроить модель для работы с различными шрифтами и форматами страниц.

**Обработка изображений:**

* Написать функцию предварительной обработки изображений (обрезка, исправление перспективы, улучшение контраста).

**Интеграция с приложением:**

* Реализовать API, которое принимает изображение, извлекает данные и возвращает результат (название, автор и т.д.).

**Тестирование модели:**

* Проверить точность извлечения данных на различных книгах и форматах.

**Бекенд:**

**Разработка серверной части приложения:**

* Создатьl API для взаимодействия с базой данных и модулем ИИ.

**Интеграция с фронтендом:**

* Настроить обмен данными между приложением и сервером; интеграция с фронтендом.

**Обеспечение безопасности:**

* Реализовать защиту данных.  
* Настроить аутентификацию и авторизацию пользователей, если требуется.

**Тестирование серверной части:**

* Проверить корректность обработки запросов.  
* Обеспечить обработку ошибок (например, если книга не найдена).

План работы:

Январь:

Подготовка и сбор теоретического материала. Обзор и анализ предметной области, актуальность, формирование требований, выбор методов и технологий разработки.

Кира:

* Анализ инструментов для проектирования БД.  
* Определение требований к БД.  
* Определение требований к маркировке.  
* Изучение технологий для фронтенда. (Kotlin, Jetpack compose)

Диана:

* Исследование методов компьютерного зрения для извлечения текста из изображений.  
* Выбор технологий для бекенда.  
* Анализ существующих решений для распознавания текста.

Совместно:

* Создание документа с требованиями к приложению.  
* Разработка архитектурного плана.

Февраль:

Выступление на конференции в колледже. Формирование прототипа проекта.

Кира:

* Проектирование интерфейса.

Совместно:

* Подготовка презентации.

Март:

Учебная практика в колледже (03.03.25 \- 21.03.25)  
Производственная практика (24.03.25 \- 18.04.25)

**Кира:**

* Разработка и редактирование интерфейса пользователя на основе ранее разработанного приложения.  
* Настройка навигации и основных экранов.  
* Реализация доступа к камере и внесения данных на уровне интерфейса.

**Диана:**

* Исследование и внедрение модели ИИ для извлечения текста и данных из изображений.  
* Интеграция модуля ИИ с тестовой базой данных.  
* Настройка и разработка API.

Апрель:

Производственная практика (24.03.25 \- 18.04.25)  
Преддипломная практика (21.04.25 \- 16.05.25)

**Кира:**

* Подключение интерфейса к бэкендуI.  
* Разработка модуля формирования маркировки (генерация штрихкодов или QR-кодов для печати).

**Диана:**

* Оптимизация работы модуля ИИ для обработки различных форматов книг.  
* Настройка базы данных для быстрого поиска и обновления информации о книгах.

**Совместно:**

* Проведение интеграционного тестирования.  
* Подготовка промежуточного отчета о проделанной работе.

Май:

Преддипломная практика (21.04.25 \- 16.05.25)  
**Кира:**

* Завершение работы над интерфейсом.  
* Настройка функционала формирования маркировок и отправки на печать..  
* Тестирование фронтенда на реальных данных.

**Диана:**

* Оптимизация производительности модуля ИИ.  
* Обеспечение надежности базы данных и бекенда.

**Совместно:**

* Финальная сборка приложения.  
* Полное тестирование всех компонентов на устойчивость и удобство.  
* Подготовка презентации и демонстрации приложения.
