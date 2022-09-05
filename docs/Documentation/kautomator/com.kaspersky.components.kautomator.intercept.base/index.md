//[kautomator](../index.md)/[com.kaspersky.components.kautomator.intercept.base](index.md)



# Package com.kaspersky.components.kautomator.intercept.base  


## Types  
  
|  Name|  Summary| 
|---|---|
| [UiInterceptable](-ui-interceptable/index.md)| [androidJvm]  <br>Content  <br>interface [UiInterceptable](-ui-interceptable/index.md)<[Interaction](-ui-interceptable/index.md), [Assertion](-ui-interceptable/index.md), [Action](-ui-interceptable/index.md)>  <br><br><br>
| [UiInterception](-ui-interception/index.md)| [androidJvm]  <br>Content  <br>data class [UiInterception](-ui-interception/index.md)<[T](-ui-interception/index.md)>(**isOverride**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), **interceptor**: [T](-ui-interception/index.md))  <br><br><br>
| [UiInterceptor](-ui-interceptor/index.md)| [androidJvm]  <br>Brief description  <br><br><br><br><br>Base class for intercepting the call chain from Kautomator to UiAutomator.<br><br><br><br>Interceptors can be provided through [KautomatorConfigurator](../com.kaspersky.components.kautomator/-kautomator-configurator/index.md) runtime, different com.kaspersky.components.kautomator.component.screen.UiScreen as well as [UiViews](../com.kaspersky.components.kautomator.component.common.views/-ui-base-view/index.md).<br><br><br><br>Interceptors are stacked during the runtime for any UiAutomator_DSL-UiAutomator check and perform operations. The stack ordering is following: UiView interceptor -> UiScreen interceptors -> UiAutomatorDsl interceptor.<br><br><br><br>Any of the interceptors in the chain can break the chain call by setting isOverride to true in onCheck, onPerform or onAll interception functions during the configuration. Doing this will not only prevent underlying interceptors from being invoked, but prevents UiAutomator from executing the operation. In that case, responsibility for actually making UiAutomator call lies on developer.<br><br><br><br>For each operation the interceptor invocation cycle will be as follows:<br><br>// For check operation  <br>onAll?.invoke()  <br>onCheck?.invoke()  <br>  <br>// For perform operation  <br>onAll?.invoke()  <br>onPerform?.invoke()<br><br>  <br>Content  <br>class [UiInterceptor](-ui-interceptor/index.md)<[Interaction](-ui-interceptor/index.md), [Assertion](-ui-interceptor/index.md), [Action](-ui-interceptor/index.md)>(**onCheck**: [UiInterception](-ui-interception/index.md)<([Interaction](-ui-interceptor/index.md), [Assertion](-ui-interceptor/index.md)) -> [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)>?, **onPerform**: [UiInterception](-ui-interception/index.md)<([Interaction](-ui-interceptor/index.md), [Action](-ui-interceptor/index.md)) -> [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)>?, **onAll**: [UiInterception](-ui-interception/index.md)<([Interaction](-ui-interceptor/index.md)) -> [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)>?)  <br><br><br>
