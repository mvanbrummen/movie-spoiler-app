import React, { Component } from 'react';
import './App.css';

export default class App extends Component {

  constructor(props) {
    super(props);

    this.state = { movieTitle: '', spoiler: '' }
  }

  getSpoiler = () => {
    fetch('/spoiler')
      .then(res => res.json())
      .then(data => {
        this.setState({
          movieTitle: data.movieTitle,
          spoiler: data.spoiler
        });
      })
      .catch(e => {
        console.log('Error:' + e);
      })
  }

  render() {
    return (
      <div>
        <header className="app-header">
          <h1 className="app-title">Movie Spoilers</h1>
        </header>
        <section className="app-body">
          <h1 className="title">
            {this.state.movieTitle}
          </h1>
          <p className="spoiler-text">
            {this.state.spoiler}
          </p>
          <input className="button" type="button" value="RANDOM SPOILER" onClick={this.getSpoiler} />
        </section>
      </div>
    );
  }
}
