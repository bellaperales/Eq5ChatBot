

import React, { useState } from "react";

function NewItem(props) {
  const [item, setItem] = useState('');
  function handleSubmit(e) {
    // console.log("NewItem.handleSubmit("+e+")");
    if (!item.trim()) {
      return;
    }
    // addItem makes the REST API call:
    props.addItem(item);
    setItem("");
    e.preventDefault();
  }
  function handleChange(e) {
    setItem(e.target.value);
  }
  return (
    <div id="newinputform">
      <form>
        <input
          id="newiteminput"
          placeholder="New item"
          type="text"
          autoComplete="off"
          value={item}
          onChange={handleChange}
          // No need to click on the "ADD" button to add a todo item. You
          // can simply press "Enter":
          onKeyDown={event => {
            if (event.key === 'Enter') {
              handleSubmit(event);
            }
          }}
        />
        <span>&nbsp;&nbsp;</span>
        <button
          className="AddButton"
          disabled={props.isInserting}
          onClick={!props.isInserting ? handleSubmit : null}
        >
          {props.isInserting ? 'Adding…' : 'Add'}
        </button>
      </form>
    </div>
  );
}

export default NewItem;