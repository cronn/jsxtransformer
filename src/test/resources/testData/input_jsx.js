'use strict';

var MyComponent = React.createClass({
	render: function()
	{
		return <h1>Hello World!</h1>
	}
});

ReactDOM.render(
	<MyComponent />,
	document.getElementById('myDiv')
);
