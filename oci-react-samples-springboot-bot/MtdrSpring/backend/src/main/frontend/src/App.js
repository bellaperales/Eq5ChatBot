import React, { useState, useEffect } from 'react';
import NewItem from './NewItem';
import API_LIST from './API';
import DeleteIcon from '@mui/icons-material/Delete';
import { Button, TableBody, CircularProgress } from '@mui/material';
import Moment from 'react-moment';

function App() {
  const [isLoading, setLoading] = useState(false);
  const [isInserting, setInserting] = useState(false);
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState();

  function deleteTask(deleteId) {
    fetch(`${API_LIST}/tasks/${deleteId}`, {
      method: 'DELETE',
    })
      .then(response => {
        if (response.ok) {
          return response;
        } else {
          throw new Error('Something went wrong ...');
        }
      })
      .then(
        (result) => {
          const remainingTasks = tasks.filter(task => task.id !== deleteId);
          setTasks(remainingTasks);
        },
        (error) => {
          setError(error);
        }
      );
  }

  function toggleStatus(event, id, status) {
    event.preventDefault();
    modifyTask(id, status).then(
      (result) => { reloadOneTask(id); },
      (error) => { setError(error); }
    );
  }

  function reloadOneTask(id) {
    fetch(`${API_LIST}/tasks/${id}`)
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Something went wrong ...');
        }
      })
      .then(
        (result) => {
          const updatedTasks = tasks.map(task => (task.id === id ? result : task));
          setTasks(updatedTasks);
        },
        (error) => {
          setError(error);
        }
      );
  }

  function modifyTask(id, status) {
    var data = { "status": status };
    return fetch(`${API_LIST}/tasks/${id}`, {
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
    fetch(`${API_LIST}/tasks`)
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
          setTasks(result);
        },
        (error) => {
          setLoading(false);
          setError(error);
        }
      );
  }, []);

  function addTask(text) {
    console.log("addTask(" + text + ")")
    setInserting(true);
    var data = {
      "name": text,
      "description": "",
      "status": 0,
      "datelimit": new Date().toISOString(),
      "type": "",
      "employeeid": null,
      "projectid": null
    };
    fetch(`${API_LIST}/tasks`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data),
    }).then((response) => {
      console.log(response);
      console.log(response.headers.location);
      if (response.ok) {
        return response;
      } else {
        throw new Error('Something went wrong ...');
      }
    }).then(
      (result) => {
        var id = result.headers.get('location');
        var newTask = { "id": id, "name": text, "status": 0 };
        setTasks([newTask, ...tasks]);
        setInserting(false);
      },
      (error) => {
        setInserting(false);
        setError(error);
      }
    );
  }

  return (
    <div className="App">
      <h1>MY TODO LIST</h1>
      <NewItem addTask={addTask} isInserting={isInserting} />
      {error &&
        <p>Error: {error.message}</p>
      }
      {isLoading &&
        <CircularProgress />
      }
      {!isLoading &&
        <div id="maincontent">
          <table id="tasklistNotDone" className="tasklist">
            <TableBody>
              {tasks.map(task => (
                task.status === 0 && (
                  <tr key={task.id}>
                    <td className="name">{task.name}</td>
                    <td className="date"><Moment format="MMM Do hh:mm:ss">{task.datecreated}</Moment></td>
                    <td>
                      <Button variant="contained" className="DoneButton" onClick={(event) => toggleStatus(event, task.id, 1)} size="small">
                        Done
                      </Button>
                    </td>
                  </tr>
                )
              ))}
            </TableBody>
          </table>
          <h2 id="donelist">
            Done tasks
          </h2>
          <table id="tasklistDone" className="tasklist">
            <TableBody>
              {tasks.map(task => (
                task.status === 1 && (
                  <tr key={task.id}>
                    <td className="name">{task.name}</td>
                    <td className="date"><Moment format="MMM Do hh:mm:ss">{task.datecreated}</Moment></td>
                    <td>
                      <Button variant="contained" className="DoneButton" onClick={(event) => toggleStatus(event, task.id, 0)} size="small">
                        Undo
                      </Button>
                    </td>
                    <td>
                      <Button startIcon={<DeleteIcon />} variant="contained" className="DeleteButton" onClick={() => deleteTask(task.id)} size="small">
                        Delete
                      </Button>
                    </td>
                  </tr>
                )
              ))}
            </TableBody>
          </table>
        </div>
      }
    </div>
  );
}

export default App;