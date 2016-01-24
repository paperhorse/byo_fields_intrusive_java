# byo_fields_intrusive_java
Intrusive data structures in java
=================================

STATUS: under construction

Intrusive data structures have their linking pointers embedded in their data nodes. They are how you naturally do you data structures if you hand write your algorithms in C. Libraries almost always use non-intrusive data structures where the node's have only link pointers plus another pointer to the data object.

Intrusive data structures are more combinable. Java coded LinkedHashMap after they already had made LinkedList and HashMap. They needed to make it separately because non-intrusive data structures don't combine efficiently.

