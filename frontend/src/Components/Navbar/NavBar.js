import React from 'react';
import MyImage from "../../logo.jpg";
import './NavBar.css';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';

const NavBarComponent = () => {
    return (
        <Navbar expand="lg" className="bg-body-tertiary">
            <img className={'navbar-logo'} src={MyImage} alt="logo"/>
            <div className="navbar-flex-container">
                <Navbar.Toggle aria-controls="navbar-toggle" />
                <Navbar.Collapse className="navbar-collapse">
                    <Nav className="attorneys">
                        <NavDropdown title="Attorneys" className="attorneys-dropdown">
                            <NavDropdown.Item href="/attorneys/overview">Overview</NavDropdown.Item>
                            <NavDropdown.Item href="/attorneys/add-attorney">Add Attorney</NavDropdown.Item>
                            <NavDropdown.Item href="/attorneys/add-service-pricing">Add Service Pricing</NavDropdown.Item>
                            <NavDropdown.Item href="/attorneys/delete-service-pricing">Delete Service Pricing</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    <Nav className="clients">
                        <NavDropdown title="Clients" className="clients-dropdown">
                            <NavDropdown.Item href="/clients/overview">Overview</NavDropdown.Item>
                            <NavDropdown.Item href="/clients/add-client">Add Client</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    <Nav className="cases">
                        <NavDropdown title="Cases" className="cases-dropdown">
                            <NavDropdown.Item href="/cases/overview">Overview</NavDropdown.Item>
                            <NavDropdown.Item href="/cases/add-case">Add Cases</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    <Nav className="services">
                        <NavDropdown title="Services" className="services-dropdown">
                            <NavDropdown.Item href="/services/overview">Overview</NavDropdown.Item>
                            <NavDropdown.Item href="/services/add-service">Add Services</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    <Nav className="disbursements">
                        <NavDropdown title="Disbursements" className="disbursements-dropdown">
                            <NavDropdown.Item href="/disbursements/overview">Overview</NavDropdown.Item>
                            <NavDropdown.Item href="/disbursements/add-disbursement">Add Disbursements</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            </div>
        </Navbar>
    );
}

export default NavBarComponent;