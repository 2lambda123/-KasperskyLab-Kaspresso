# Ваш первый тест на Kaspresso

<br> Прежде чем приступать к написанию теста, давайте поближе познакомимся с функционалом, который мы будем покрывать автотестами. 

<br> Переключаемся на ветку `issue-372/update_tutorial`. В ней находится исходный код самого приложения без тестов. В этом и последующих уроках будет приведена пошаговая инструкция в формате code labs по написанию автотестов. Итоговый результат доступен в ветке `tutorial_results`.

<br> Открываем выбор конфигурации (1) и выбираем tutorial (2):

<img src="../images/simple_test/Select_tutorial.png" alt="Select tutorial"/>

<br> Проверяем, что выбран нужный девайс (1) и запускаем приложение (2):

<img src="../images/simple_test/Launch_tutorial.png" alt="Launch tutorial"/>

<br> После успешного запуска приложения мы видим основной экран приложения Tutorial.

<img src="../images/simple_test/Tutorial_main.png" alt="Tutorial main" width="200"/>

<br> Нажимаем на кнопку с текстом "Simple test" и видим следующий экран:

<img src="../images/simple_test/First_tutorial_screen.png" alt="Page object example" width="200"/>

<br> Экран состоит из:
<br> 1. *Заголовка `TextView`*
<br> 2. *Поля ввода `EditText`*
<br> 3. *Кнопки `Button`*

!!! info
    Полный список виджетов в android с подробной информацией можно найти [здесь](https://developer.android.com/reference/android/widget/package-summary)

<br> При нажатии на кнопку текст в заголовке меняется на введенный в поле ввода.

<br> Чтобы покрыть приложение тестами Kaspresso, необходимо начать с подключения Kaspresso в зависимостях проекта.

## Подключаем Kaspresso к проекту

<br> 1. Переключаем отображение файлов проекта как Project (1) и подключаем в файл `build.gradle` проекта репозиторий `mavenCentral` (2):
<img src="../images/simple_test/Project_build_gradle.png" alt="Project build gradle"/>

```groovy
allprojects {
    repositories {
        mavenCentral()
    }
}
```

<br> 2. Переключаем отображение файлов проекта как Project (1) и добавляем зависимость в файл `build.gradle` модуля `Tutorial`:

<img src="../images/simple_test/Tutorial_build_gradle.png" alt="Tutorial build gradle"/>


```groovy
dependencies {
    androidTestImplementation 'com.kaspersky.android-components:kaspresso:<latest_version>'
}
```

## Написание теста начнем с создания Page object для текущего экрана.
<br> Про паттерн PageObject в Kaspresso можно прочитать в [документации](https://kasperskylab.github.io/Kaspresso/Wiki/Page_object_in_Kaspresso/).<br/>
<br> В папке `androidTest` создаем папку `screen` и кладем туда объект `SimpleScreen`:
<br> Имея доступ к исходному коду `Tutorial` мы можем увидеть, что интересующий нас экран отображается в `SimpleActivity`, а сама верстка лежит в файле `activity_simple`. Поэтому, в данном случае мы можем воспользоваться `white-box тестированием` (детальнее про этот тип тестирования можно узнать [здесь](https://ru.wikipedia.org/wiki/%D0%A2%D0%B5%D1%81%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5_%D0%B1%D0%B5%D0%BB%D0%BE%D0%B3%D0%BE_%D1%8F%D1%89%D0%B8%D0%BA%D0%B0)). Альтернативный способ тестирования (black-box) будет рассмотрен в следующих уроках.

```kotlin
object SimpleScreen : KScreen<SimpleScreen>() {

    override val layoutId: Int = R.layout.activity_simple
    override val viewClass: Class<*> = SimpleActivity::class.java

    val title = KTextView { withId(R.id.simple_title) }
    val button = KButton { withId(R.id.change_title_btn) }
    val input = KEditText { withId(R.id.input_text) }
}
```
<br> В этом объекте мы описываем элементы интерфейса, с которым будет взаимодействовать тест. Здесь стоит обратить внимание на то, что мы один раз кладем matcher-ы в конструктор `View` ({ withId(R.id...)}). В самом тесте мы сможем обращаться к SimpleScreen и его элементам напрямую.
<br> Если автотест пишет тот же человек, кто и писал код самого приложения, то не должно возникнуть вопросов по указанным выше `id` в методах `withId`, `layoutId` и `viewClass`. В нашем же случае код Tutorial был написан до нас, поэтому нам важно понять, как их найти. При помощи [Layout inspector](https://medium.com/androiddevelopers/layout-inspector-1f8d446d048) можно проанализировать структуру элементов на экране, узнать id нужных элементов. В дальнейшем, глобальным поиском по проекту можно найти файлы `layoutId` и `viewClass`, с которыми связаны найденные `id`. Подход PageObject позволяет разделить работу по написанию автотестов между разработчиками приложения и разработчиками тестов: разработчики приложения могут создавать объекты `Screen` для нужных экранов со всеми идентификаторами элементов, а разработчики автотестов по готовым объектам моделей экранов реализовывать тесткейсы. 
<br> Для поиска нужного `View` можно использовать сразу несколько matcher-ов. Например, если у какого-то элемента нет `id`, мы можем найти его с помощью нескольких matcher-ов. 
<br> В объекте SimpleScreen переопределены `layoutId` и `ViewClass`. Если их корректно не проинициализировать (например, присвоить `null`), то на работоспособность теста это влиять не будет. Но мы рекомендуем не игнорировать их и корректно инициализировать. Это поможет при разработке и дальнейшей поддержки понимать, с каким `ViewClass` и `layoutId` связан конкретный Screen.

## Приступаем с коду самого теста

<br> В папке `androidTest` создаем класс `SimpleTest`:

```kotlin
class SimpleTest : TestCase() {

    @get:Rule
    val activityRule = activityScenarioRule<SimpleActivity>()

    @Test
    fun test() {

    }
}
```

<br> Тест `SimpleTest` можно запустить. Информацию по запуску тестов в Android Studio можно найти в  [предыдущем уроке](https://kasperskylab.github.io/Kaspresso/Tutorial/Running_the_first_test/)
<br> Этот тест осуществит запуск указанной activity `SimpleActivity` перед запуском теста и закроет после прогона теста. За это отвечает:

```kotlin
    @get:Rule
    val activityRule = activityScenarioRule<SimpleActivity>()
```

<br> Подробнее про `activityScenarioRule` можно почитать [здесь](https://developer.android.com/reference/androidx/test/ext/junit/rules/ActivityScenarioRule)

<br> SimpleTest унаследован от TestCase. Это не единственный способ создать тестовый класс. В случае, когда невозможно отнаследоваться от TestCase (в Java и Kotlin запрещено множественное наследование), можно использовать TestCaseRule. 

```kotlin
 @get:Rule
 val testCaseRule = TestCaseRule(javaClass.simpleName)
```

<br>В этом случае тело тестового метода должно начинаться с обращения к этому инстансу:

```kotlin
@Test
fun test() =
  testCaseRule.run {
      ...
  }
```

<br> Расширим код теста `test()` в `SimpleTest` проверкой, что заголовок отображается и содержит ожидаемый текст.

```kotlin
class SimpleTest : TestCase() {

    @get:Rule
    val activityRule = activityScenarioRule<SimpleActivity>()

    @Test
    fun test() {
        SimpleScreen {
            title {
                isVisible()
                hasText(R.string.simple_activity_default_title)
            }
        }
    }
}
```

<br> Обращаемся к созданному нами выше PageObject-у `SimpleScreen`. У этого объекта мы объявили поле заголовка `title`. Внутри блока `title{ }` доступны различные методы. 
<br> Сейчас нас интересуют методы `isVisible()` и `hasText()`. 
<br> Элемент `title` объявлен с типом `KTextView`. Это класс-обертка в `Kaspresso`, которая реализует интерфейсы `TextViewActions` и `TextViewAssertions`. Первый определяет набор действий, которые могут быть выполнены над заголовком, а второй - набор проверок. 
<br> Рекомендуем посмотреть код этих интерфейсов, их родителей и аналогичные интерфейсы для других элементов.

## Расширим код теста

```kotlin
class SimpleTest : TestCase() {

    @get:Rule
    val activityRule = activityScenarioRule<SimpleActivity>()

    @Test
    fun test() {
        SimpleScreen {
            title {
                isVisible()
                hasText(R.string.simple_activity_default_title)
                hasTextColor(R.color.black)
            }

            button {
                isVisible()
                withText(R.string.simple_activity_change_title_button)
                isClickable()
            }
            input {
                isVisible()
                hasHint(R.string.simple_activity_input_hint)
                hasEmptyText()

                typeText("Kaspresso")
                hasText("Kaspresso")
            }
            closeSoftKeyboard()
            button {
                click()
            }
            title {
                hasText("Kaspresso")
            }
        }
    }
}
```

<br> Рассмотрим сам тест. Благодаря реализации паттерна Page object и Kotlin DSL код теста становится простым и понятным: сперва мы проверили корректность отображения нужных элементов, затем ввели текст в поле ввода, нажали кнопку и проверили, что заголовок изменился. Однако, код любого теста - это реализация определенных тест-кейсов. Сам же тест-кейс - это некий сценарий (последовательность шагов), написанный на человеческом языке тестировщиком. Этот набор шагов может со временем меняться, поэтому спустя какое-то время возникнет потребность в редактировании теста. Помимо этого, тест может не всегда проходить успешно. Чтобы тест было легко поддерживать и он оставался понятным спустя долгое время, он должен быть разделен на шаги, идентичные указанным в тест-кейсах. Комментарии будут не самым лучшим решением, так как в логах не будет понятно, на каком шаге упал тест. Для этого можно воспользоваться специальными методами Kaspresso (например, `step()`).

```kotlin
class SimpleTest : TestCase() {

    @get:Rule
    val activityRule = activityScenarioRule<SimpleActivity>()

    @Test
    fun test() =
        before {

        }.after {

        }.run {
            step("Open Simple Screen") {
                SimpleScreen {
                    title {
                        isVisible()
                        hasText(R.string.simple_activity_default_title)
                        hasTextColor(R.color.black)
                    }

                    button {
                        isVisible()
                        withText(R.string.simple_activity_change_title_button)
                        isClickable()
                    }
                    input {
                        isVisible()
                        hasHint(R.string.simple_activity_input_hint)
                        hasEmptyText()
                    }
                }
            }

            step("Type \" Kaspresso \"") {
                SimpleScreen {
                    input.typeText("Kaspresso")
                    closeSoftKeyboard()
                    button.click()
                }
            }

            step("Check title content") {
                SimpleScreen {
                    title.hasText("Kaspresso")
                }
            }
        }
}
```
<br> У каждого теста могут быть свои предусловия (определенные состояния девайса), а после его выполнения состояние девайса должно быть возвращено в исходное. Секции before и after нужны для настройки состояния до и после прогона теста. Например, это может быть включение Bluetooth. До выполнения теста необходимо включить его, а после - выключить. Более подробно эти секции описаны в следующих примерах. 
<br> Step представляет собой абстракцию, которая реагирует на все события шага (например: шаг стартует, шаг завершается успехом или неудачей). Внутри одной секции step может быть объявлены другие секции step. По умолчанию, абстракция step добавляет логирование и скриншотинг (возможность кастомизаций набора действий описаны в следующих примерах). Таким образом, после прогона теста можно будет посмотреть подробные записи логирования, которые будут полезны для дальнейшей поддержки тестов в рабочем состоянии и устранении проблем. Скришоты будут делаться по окончании шага (по одному на каждую step-секцию) и перед завершением теста в случае возникновения ошибки. Данное поведение основывается на философии Kaspresso о возможном изменении состояния после каждого шага. Если необходимо больше скриншотов, то их можно сделать с помощью вызова метода `device.screenshots.take("Additional_screenshot")`. Доступный функционал `device` описан в следующих примерах.
