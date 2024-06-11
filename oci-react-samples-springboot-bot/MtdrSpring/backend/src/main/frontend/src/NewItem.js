/*
## MyToDoReact version 1.0.
##
## Copyright (c) 2022 Oracle, Inc.
## Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
*/
/*
 * Component that supports creating a new todo item.
 * @author  jean.de.lavarene@oracle.com
 */

import Button from '@mui/material/Button';
import React, { useState } from "react";


function NewItem(props) {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [dateLimit, setDateLimit] = useState('');
  const [type, setType] = useState('');
  
  function handleSubmit(e) {
    // console.log("NewItem.handleSubmit("+e+")");
    /* if (!item.trim()) {
      return;
    }*/
    // addItem makes the REST API call:
    props.addItem(name, description, dateLimit, type);
    setName("");
    setDescription("");
    setDateLimit("");
    setType("");
    e.preventDefault();
  }
  /*
  function handleChange(e) {
    setItem(e.target.value);
  }*/

  return (
    <div id="newinputform">
      <form
        onSubmit={(e) => {
          e.preventDefault();
          if (name.trim() && description.trim() && type.trim() && dateLimit.trim()) {
            handleSubmit();
          }
        }}
      >
        <input
          id="newiteminput"
          placeholder="Name"
          type="text"
          autoComplete="off"
          value={name}
          onChange={(e) => setName(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter' && name.trim() && description.trim() && type.trim() && dateLimit.trim()) {
              handleSubmit();
            }
          }}
        />
        <input
          id="descriptioninput"
          placeholder="Description"
          type="text"
          autoComplete="off"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter' && name.trim() && description.trim() && type.trim() && dateLimit.trim()) {
              handleSubmit();
            }
          }}
        />
        <input
          id="datelimitinput"
          placeholder="Date Limit"
          type="date"
          value={dateLimit}
          onChange={(e) => setDateLimit(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter' && name.trim() && description.trim() && type.trim() && dateLimit.trim()) {
              handleSubmit();
            }
          }}
        />
        <input
          id="typeinput"
          placeholder="Type"
          type="text"
          autoComplete="off"
          value={type}
          onChange={(e) => setType(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter' && name.trim() && description.trim() && type.trim() && dateLimit.trim()) {
              handleSubmit();
            }
          }}
        />
        <span>&nbsp;&nbsp;</span>
        <Button
          className="AddButton"
          variant="contained"
          disabled={props.isInserting}
          type="submit"
          size="small"
        >
          {props.isInserting ? 'Adding…' : 'Add'}
        </Button>
      </form>
    </div>
  );
}

export default NewItem;