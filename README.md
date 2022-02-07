# Description

Implementation of the trust calculation algorithm, as supposed *by G. Caronni, "Walking the Web of trust," Proceedings IEEE 9th International Workshops on Enabling Technologies: Infrastructure for Collaborative Enterprises (WET ICE 2000), 2000, pp. 153-158, doi: 10.1109/ENABL.2000.883720*.

# Input file format
```
node1,node2,node3,...

node1
othernode1:linkvalue,othernode2:linkvalue,...

node3
othernode3:linkvalue,othernode4:linkvalue,...

...
```
First line names the nodes that are present in the web.
After that, two lines for each node follow. The first list states which node is currently a `source`. The second line lists pairs of `target:value`, which means that the link between `source` and `target` has the value `value`.

## Example
```
A,B,C,D

A
C:0.4,D:0.7

C
B:0.9
```
