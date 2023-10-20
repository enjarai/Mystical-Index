```json
{
  "title": "Area Indexing",
  "icon": "minecraft:beacon",
  "category": "arcane_repository:indexing"
}
```

One of the two ways indexing books can connect to bookshelves is using area indexing.
Books will use this by default, and it is arguably the simplest option.


When using area indexing, the book will try to interact with all bookshelves within its range.
This range can be fixed (while on a lectern) or mobile (while in your inventory).

;;;;;

The range of an indexing book can be increased by adding [Range Pages](^arcane_repository:indexing/attribute_range) to it,
and can be viewed by hovering over the book in your inventory. 


*Note that the "Linked range", also shown in the tooltip, is only used by [linked indexing](^arcane_repository:indexing/mode_linked).*


The range is calculated as a square box centered on the book, sized *range\*2+1* blocks on the x, y, and z axes.