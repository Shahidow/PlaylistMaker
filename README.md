# PlaylistMaker

## Описание

Приложение для поиска музыки в библиотеке ITunes и создания плейлистов. Создавалось на курсе Яндекс Практикума в качестве практического задания.

## Системные требования

1. Java Development Kit (JDK): версия 8 или выше.

2. Android Studio: версия 2022.1 или выше.


## Версии SDK

* Compile SDK Version: 33

* Minimum SDK Version: 29

* Target SDK Version: 33


## Версия языка

* Kotlin: 1.7 или выше


## Основные зависимости проекта:

* **Kotlin Coroutines:**

  org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4

* **AndroidX Navigation:** 

androidx.navigation:navigation-fragment-ktx:2.5.3

androidx.navigation:navigation-ui-ktx:2.5.3

* **AndroidX Fragment:**

  androidx.fragment:fragment-ktx:1.5.6

* **Material Design:**

  com.google.android.material:material:1.9.0

* **ViewPager2:**

  androidx.viewpager2:viewpager2:1.0.0

* **Koin:**

  io.insert-koin:koin-android:3.3.0

* **RxJava:**

  io.reactivex.rxjava3:rxjava:3.1.8

* **Retrofit:**

com.squareup.retrofit2:retrofit:2.9.0

com.squareup.retrofit2:converter-gson:2.9.0

* **Gson:**

  com.google.code.gson:gson:2.10.1

* **Glide:**

com.github.bumptech.glide:glide:4.14.2

com.github.bumptech.glide:compiler:4.14.2

* **Room:**

androidx.room:room-runtime:2.5.1

androidx.room:room-compiler:2.5.1

androidx.room:room-ktx:2.5.1



## Инструкция по запуску

1. Клонирование репозитория:

```
git clone <URL_вашего_репозитория>

cd playlistmaker
```



2. Открытие проекта в Android Studio:

Запустите Android Studio.
Выберите "Open an existing Android Studio project".
Укажите путь к папке, куда вы клонировали проект.



3. Синхронизация зависимостей:

После открытия проекта Android Studio автоматически предложит синхронизировать зависимости. Если нет, выберите "File" -> "Sync Project with Gradle Files".



4. Настройка эмулятора или устройства:

Убедитесь, что у вас настроен Android Virtual Device (AVD) или подключено реальное устройство.
Выберите целевое устройство в панели инструментов.



5. Запуск приложения:

Нажмите на зеленую кнопку "Run" (или используйте комбинацию Shift + F10).
Выберите устройство и дождитесь завершения сборки и установки приложения.



6. Проверка работоспособности:

После установки приложение должно запуститься. Проверьте основные функции, чтобы убедиться, что всё работает корректно.



## Примечания

Если возникнут проблемы с зависимостями, убедитесь, что у вас установлены все необходимые плагины в Android Studio. Проверьте наличие доступа к интернету.
