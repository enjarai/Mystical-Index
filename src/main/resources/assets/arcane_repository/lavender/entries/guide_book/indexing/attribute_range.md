```json
{
  "title": "Attribute: Range",
  "icon": "arcane_repository:range_page",
  "category": "arcane_repository:indexing",
  "associated_items": [
    "arcane_repository:range_page"
  ],
  "ordinal": 4
}
```

Range Attribute Pages can be added to [indexing](^arcane_repository:indexing/type_indexing) and 
[index extension](^arcane_repository:indexing/type_index_slave) books to increase both their
[linked](^arcane_repository:indexing/mode_linked) and [area](^arcane_repository:indexing/mode_area) range.


Every range page added will double the range of your book, stacking exponentially.


<|item-spotlight@lavender:book_components|item=arcane_repository:range_page|>

;;;;;

Both types of ranges are calculated as a square box centered on the book, 
sized *range\*2+1* blocks on the x, y, and z axes.

<recipe;arcane_repository:page/attribute/range>