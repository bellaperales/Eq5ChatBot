/*
## MyToDoReact version 1.0.
##
## Copyright (c) 2022 Oracle, Inc.
## Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
*/
/*
 * This is the application main React component. We're using "function"
 * components in this application. No "class" components should be used for
 * consistency.
 * @author  jean.de.lavarene@oracle.com
 */
import React, { useState, useEffect } from 'react';
import NewItem from './NewItem';
import API_LIST from './API';
import DeleteIcon from '@mui/icons-material/Delete';
import { TableBody, CircularProgress } from '@mui/material';
import Moment from 'react-moment';

function App() {
  // isLoading is true while waiting for the backend to return the list
  // of items. We use this state to display a spinning circle:
  const [isLoading, setLoading] = useState(false);
  // Similar to isLoading, isInserting is true while waiting for the backend
  // to insert a new item:
  const [isInserting, setInserting] = useState(false);
  // The list of todo items is stored in this state. It includes the "done"
  // "not-done" items:
  const [items, setItems] = useState([]);
  // In case of an error during the API call:
  const [error, setError] = useState();

  function deleteItem(deleteId) {
    // console.log("deleteItem("+deleteId+")")
    fetch(API_LIST + "/" + deleteId, {
      method: 'DELETE',
    })
      .then(response => {
        // console.log("response=");
        // console.log(response);
        if (response.ok) {
          // console.log("deleteItem FETCH call is ok");
          return response;
        } else {
          throw new Error('Something went wrong ...');
        }
      })
      .then(
        (result) => {
          const remainingItems = items.filter(item => item.id !== deleteId);
          setItems(remainingItems);
        },
        (error) => {
          setError(error);
        }
      );
  }

  function toggleDone(event, id, description, done) {
    event.preventDefault();
    modifyItem(id, description, done).then(
      (result) => { reloadOneIteam(id); },
      (error) => { setError(error); }
    );
  }

  function reloadOneIteam(id) {
    fetch(API_LIST + "/" + id)
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Something went wrong ...');
        }
      })
      .then(
        (result) => {
          const items2 = items.map(
            x => (x.id === id ? {
              ...x,
              'description': result.description,
              'done': result.done
            } : x));
          setItems(items2);
        },
        (error) => {
          setError(error);
        });
  }

  function modifyItem(id, description, done) {
    var data = { "description": description, "done": done };
    return fetch(API_LIST + "/" + id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
      .then(response => {
        if (response.ok) {
          return response;
        } else {
          throw new Error('Something went wrong ...');
        }
      });
  }

  useEffect(() => {
    setLoading(true);
    fetch(API_LIST)
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Something went wrong ...');
        }
      })
      .then(
        (result) => {
          setLoading(false);
          setItems(result);
        },
        (error) => {
          setLoading(false);
          setError(error);
        });
  }, []); // empty deps array [] means this useEffect will run once

  function addItem(text) {
    console.log("addItem(" + text + ")");
    setInserting(true);
    var data = {};
    console.log(data);
    data.description = text;
    fetch(API_LIST, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data),
    }).then((response) => {
      console.log(response);
      console.log();
      console.log(response.headers.location);
      if (response.ok) {
        return response;
      } else {
        throw new Error('Something went wrong ...');
      }
    }).then(
      (result) => {
        var id = result.headers.get('location');
        var newItem = { "id": id, "description": text };
        setItems([newItem, ...items]);
        setInserting(false);
      },
      (error) => {
        setInserting(false);
        setError(error);
      }
    );
  }

  return (
    <div className="container">
      <div className="container__left">
        <div className="left-content-wrapper">
          <div className="left-content">
            <div className="heading-container">
              <h1>MY TO DO LIST</h1>
            </div>
            <div className="App">
              <NewItem addItem={addItem} isInserting={isInserting} />
              {error && <p>Error: {error.message}</p>}
              {isLoading && <CircularProgress />}
              {!isLoading && (
                <div id="maincontent">
                  <table id="itemlistNotDone" className="itemlist">
                    <TableBody>
                      {items.map(
                        (item) =>
                          !item.done && (
                            <tr key={item.id}>
                              <td className="description">{item.description}</td>
                              <td className="date">
                                <Moment format="MMM Do hh:mm:ss">{item.createdAt}</Moment>
                              </td>
                              <td>
                                <button
                                  className="DoneButton"
                                  onClick={(event) =>
                                    toggleDone(event, item.id, item.description, !item.done)
                                  }
                                >
                                  Done
                                </button>
                              </td>
                            </tr>
                          )
                      )}
                    </TableBody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
      <div className="container__right">
      <div className="left-content">
            <div className="heading-container">
            <h1 id="donelist">DONE ITEMS</h1>
            </div>
        <div className="App">
          <table id="itemlistDone" className="itemlist">
            <TableBody>
              {items.map(
                (item) =>
                  item.done && (
                    <tr key={item.id}>
                      <td className="description">{item.description}</td>
                      <td className="date">
                        <Moment format="MMM Do hh:mm:ss">{item.createdAt}</Moment>
                      </td>
                      <td>
                        <button
                          className="DoneButton"
                          onClick={(event) => toggleDone(event, item.id, item.description, !item.done)}
                        >
                          Undo
                        </button>
                      </td>
                      <td>
                        <button className="DeleteButton" onClick={() => deleteItem(item.id)}>
                          <DeleteIcon />
                          Delete
                        </button>
                      </td>
                    </tr>
                  )
              )}
            </TableBody>
          </table>
        </div>
      </div>
    </div>
    </div>
  );
}

export default App;