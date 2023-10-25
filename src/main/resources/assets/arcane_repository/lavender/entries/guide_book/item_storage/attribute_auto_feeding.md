```json
{
  "title": "Attribute: Auto Feeding",
  "icon": "arcane_repository:auto_feeding_attribute_page",
  "category": "arcane_repository:item_storage",
  "associated_items": [
    "arcane_repository:auto_feeding_attribute_page"
  ],
  "ordinal": 7
}
```

The auto feeding attribute page is one of the most powerful attribute pages.
When added to a [food storage](^arcane_repository:item_storage/type_food_storage) book, 
it will automatically feed the player from the contents of the book when they are hungry,
so long as the book is anywhere in their inventory.
<|item-spotlight@lavender:book_components|item=arcane_repository:auto_feeding_attribute_page|>

;;;;;

The book will feed the player when either the selected food item can be fully utilized,
or when the player drops below 7 nuggets. E.g. melon slices will be eaten when 1 nugget is depleted,
while steak will be eaten after 3 nuggets.

<recipe;arcane_repository:page/attribute/auto_feeding>