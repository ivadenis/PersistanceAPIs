/**
 * @file The main logic for the Todo List App.
 * @author Matt West <matt.west@kojilabs.com>
 * @license MIT {@link http://opensource.org/licenses/MIT}.
 */


window.onload = function() {
  
  // Display the todo items.
  todoDB.open(initialize);

  // Get references to the form elements.
  var newTodoForm = document.getElementById('new-todo-form');
  var newTodoInput = document.getElementById('new-todo');
  var newCategoryInput = document.getElementById('new-category');
  var categoryFilter = document.getElementById('categories');


/*  categorySelector.onchange = function(e) {
    e.preventDefault();
    var input = filterInput.value;

    if (isEmpty(input)) {
      todoDB.
    }

  };*/


  // Handle new todo item form submissions.
  newTodoForm.onsubmit = function(e) {
    e.preventDefault();
    // Get the todo text.
    var text = newTodoInput.value;
    var cat  = newCategoryInput.value;
    
    // Check to make sure the text is not blank (or just spaces).
    if ( isEmpty(text) && isEmpty(cat) ) {
      // Create the todo item.
      todoDB.createTodo(text, cat, function(todo) {
        refreshTodos();
        refreshCategories();
    
      });
    }
    // Reset the input field.
    newCategoryInput.value = '';
    newTodoInput.value = '';
    newTodoInput.focus();
    // Don't send the form.
    return false;
  };
  
}

/**
*
* Helpers
*
**/

function initialize (e) {
  refreshTodos('all');
  refreshCategories();
}

function isEmpty(str) {
  return str.replace(/ /g,'') != '';
}

// el - 'select' element where to append 'option'
function renderCategory(el, cat) {
  if (!el) { console.log('element needs to be provided!'); return; }
  var option = document.createElement('option');
  option.value = cat;
  option.innerHTML = cat;
  el.appendChild(option);  
}

function refreshCategories() {
  var selectField = document.getElementById('categories');
  selectField.innerHTML = '';

  var all = document.createElement('option');
  all.value = 'all';
  all.innerHTML = '--- show all ---';
  selectField.appendChild(all);

  selectField.onchange = onCategoryChange;
  todoDB.fetchCategories(function(categories) {    
    _.each(categories, function(cat, index, list) {
      renderCategory(selectField, cat);
    })
  });
}

function onCategoryChange (e) {
  e.preventDefault();
  var filter = e.target.value;
  refreshTodos(filter);
  return;
}

// Update the list of todo items.
function refreshTodos(filter) {
  if(filter === undefined) { filter = 'all'; }
  todoDB.fetchTodos(filter, function(todos) {
    var todoList = document.getElementById('todo-items');
    todoList.innerHTML = '';
    
    for(var i = 0; i < todos.length; i++) {
      // Read the todo items backwards (most recent first).
      var todo = todos[(todos.length - 1 - i)];

      var li = document.createElement('li');
      var checkbox = document.createElement('input');
      checkbox.type = "checkbox";
      checkbox.className = "todo-checkbox";
      checkbox.setAttribute("data-id", todo.timestamp);
      
      li.appendChild(checkbox);
      
      var span = document.createElement('span');
      span.innerHTML = todo.text;
      li.appendChild(span);

      /*==========  adding category to todo item html  ==========*/      
      var categoryHtml = document.createElement('span');
      categoryHtml.innerHTML = " (" + todo['category'] + ")";
      li.appendChild(categoryHtml);
      
      
      todoList.appendChild(li);
      
      // Setup an event listener for the checkbox.
      checkbox.addEventListener('click', function(e) {
        var id = parseInt(e.target.getAttribute('data-id'));

        todoDB.deleteTodo(id, initialize);
      });
    }

  });
}



