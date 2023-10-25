```json
{
  "title": "Attribute: Pickup",
  "icon": "arcane_repository:pickup_attribute_page",
  "category": "arcane_repository:item_storage",
  "associated_items": [
    "arcane_repository:pickup_attribute_page"
  ],
  "ordinal": 5
}
```

An item storage or indexing book with a pickup attribute page will 
automatically intercept item pickups when carried by a player.
Assuming there is space for the item, it'll be stored in the book instead of the player's inventory.


<|item-spotlight@lavender:book_components|item=arcane_repository:pickup_attribute_page|>

;;;;;

Item storage books can be filtered as usual to restrict what items are picked up.


It is also possible for these books to independently pick up items when placed on a lectern,
which can be quite useful for inserting items into your repository.

<recipe;arcane_repository:page/attribute/pickup>