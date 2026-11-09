# SimpleGP
SimpleGP is a genetic programming library written in Java.

It aims to achieve rich type safety avoiding uncertainty and runtime errors as much as possible.
Currently the library is bogged down by an overuse of generics to achieve type guarantees, hopefully this improves in the future.

It aims to be simple to use, enabling easy configuration and extension. It also aims to allow for "easy" parallelization.

## Design Ethos:
Library design is tricky so it is worth establishing a good ethos to center the design.
### Extensibility
This library is designed for genetic programming research. Research often involves either:
- Extending an existing approach, or
- Adding a new step.

This means that both the GP process itself must be flexible to the insertion or removal of new steps, and that it must be easy to extend existing approaches.

In traditional Object-Oriented approaches, this extensibility is typically achieved with inheritance. This library believes that extensibility is best achieved by composition. While it is not allergic to inheritance, deep inheritance can lead to complex hidden behaviour which causes bugs. 
### Safety & Predictability
Programs should never continue if they enter into an invalid state. This differs 


This manifests as a _Fail Fast_ philosophy by default, this means that ends up in an invalid state should be terminated. 
This is quite different to the standard approach in other GP libraries so it requires some justification.
The standard approach runs should _never_ fail. This a _Defensive_ design strategy. While this might sound very tempting, the idea of losing a run, especially a long one, can be quite upsetting.
However, consider the much more dangerous case where a run ends up in an invalid state and collapses. This could lead to a whole experiment providing worse results than it should have creating invalid results and comparisons across different methods.

However, SimpleGP respects that it may be undesirable to have programs blow up if you are unlucky, therefore it should be easy toggle into and out of "Defensive" mode if you need to.


#### Errors
Overall errors naturally come up in, especially in stochastic systems, so it is important that we know how to handle it.
 - Throw, while this result
 - Optional, this passes the error onto the user, theoretically safe but if the correct behaviour is to throw we lose the error message and context that created that error message.
 - Result, similar to optional, but can encode some of the error information
 - Or with a "outcome matcher".