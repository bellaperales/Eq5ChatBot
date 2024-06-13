import React, { useState, useEffect } from 'react';
import NewItem from './NewItem';
import API_LIST from './API';
import DeleteIcon from '@mui/icons-material/Delete';
import { TableBody, CircularProgress } from '@mui/material';
import Moment from 'react-moment';
import moment from 'moment-timezone';

function App() {
  const [isLoading, setLoading] = useState(false);
  const [isInserting, setInserting] = useState(false);
  const [items, setItems] = useState([]);
  const [error, setError] = useState();

  const currentProjectId = 2; // Cambia esto por el ID del proyecto que desees mostrar

  function deleteItem(deleteId) {
    fetch(${API_LIST}/${deleteId}, {
      method: 'DELETE',
    })
      .then(response => {
        if (response.ok) {
          return response;
        } else {
          throw new Error('Something went wrong while deleting ...');
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
      ).catch((error) => {
        console.error('Error:', error);
      });
  }

  function toggleDone(event, id, description, isDone) {
    const isDone = isDone; // Declarar la variable isDone
    event.preventDefault();
    const status = isDone ? 1 : 0;
    modifyItem(id, description, status)
      .then(updatedItem => {
        const updatedItems = items.map(item => (item.id === id ? updatedItem : item));
        setItems(updatedItems);
      })
      .catch(error => {
        setError(error);
        console.error('Error:', error);
      });
  }

  function reloadItems() {
    setLoading(true);
    fetch(${API_LIST}/project/${currentProjectId})
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Something went wrong while fetching items...');
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
        }).catch((error) => {
          console.error('Error:', error);
        });
  }

  function modifyItem(id, description, status) {
    const data = { description, status };
    return fetch(${API_LIST}/${id}, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Something went wrong while modifying ...');
        }
      })
      .then(updatedItem => {
        return {
          ...updatedItem,
          description,
          status,
        };
      });
  }

  useEffect(() => {
    reloadItems();
  }, []);

  function formatDateToTimestampWithTimeZone(date) {
    let formattedDate = moment(date).set({ hour: 23, minute: 59, second: 59 }).utc();
    return formattedDate.format('YYYY-MM-DDTHH:mm:ss') + 'Z';
  }

  function addItem(text, date) {
    setInserting(true);
    const data = {
      name: text,
      description: text,
      status: 0,
      dateLimit: formatDateToTimestampWithTimeZone(date),
      type: 'Desarrollo',
      employeeID: 1,
      projectID: currentProjectId
    };
    fetch(API_LIST, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data),
    }).then(response => {
      if (!response.ok) {
        throw new Error('Something went wrong while adding...');
      }
      return response.json();
    })
      .then(result => {
        const newItem = {
          id: result.id,
          name: text,
          description: text,
          status: 0,
          dateLimit: formatDateToTimestampWithTimeZone(date),
          type: 'Desarrollo',
          employeeID: 1,
          projectID: currentProjectId
        };
        setItems(items.concat([newItem]));
        setInserting(false);
      })
      .catch(error => {
        setInserting(false);
        setError(error);
        console.error('Error:', error);
      });
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
                      {items
                        .filter(item => item.status === 0)
                        .map(item => (
                          <tr key={item.id} className="task-item">
                            <td className="description">{item.description}</td>
                            <td className="date">
                              <Moment format="MMM Do hh:mm:ss">{item.dateLimit}</Moment>
                            </td>
                            <td>
                              <button
                                className="DoneButton"
                                onClick={event => toggleDone(event, item.id, item.description, true)}
                              >
                                Done
                              </button>
                            </td>
                          </tr>
                        ))}
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
                {items
                  .filter(item => item.status === 1)
                  .map(item => (
                    <tr key={item.id} className="task-item">
                      <td className="description">{item.description}</td>
                      <td className="date">
                        <Moment format="MMM Do hh:mm:ss">{item.dateLimit}</Moment>
                      </td>
                      <td>
                        <button
                          className="DoneButton"
                          onClick={event => toggleDone(event, item.id, item.description, false)}
                        >
                          Undo
                        </button>
                      </td>
                      <td>
                        <button className="DeleteButton" onClick={() => deleteItem(item.id)}>
                          <DeleteIcon />
                        </button>
                      </td>
                    </tr>
                  ))}
              </TableBody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;