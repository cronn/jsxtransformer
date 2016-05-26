'use strict';

var MyComponent = React.createClass({ displayName: 'MyComponent', 
	render: function render() 
	{
		return React.createElement('h1', null, 'Hello World!');} });



ReactDOM.render(
React.createElement(MyComponent, null), 
document.getElementById('myDiv'));
