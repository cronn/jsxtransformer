'use strict';

var MyComponent = React.createClass({
	render: function()
	{
		return <h1>Hallo Welt!</h1>
	}
});

ReactDOM.render(
	<MyComponent />,
	document.getElementById('myDiv')
);
