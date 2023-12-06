import React from "react";

export default function LandingPage() {
    return (
        <div class="d-grid gap-2 col-6 mx-auto">
            <h1>Welcome to PoorPal</h1>
            <a href="/login" class="btn btn-primary" role="button">Login</a>
            <a href="/registration" class="btn btn-primary" role="button">Register</a>
        </div>
    );
}
