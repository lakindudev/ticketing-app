import { useState } from 'react'

function TicketConfig() {
    const [config, setConfig] = useState({
        totalTickets: 0,
        releaseRate: 0,
        retrievalRate: 0,
        maxCapacity: 0
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        
        fetch('http://localhost:8080/ticket-config', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(config)
        })
        .then(response => {
            if (response.ok) {
                alert('Configuration submitted successfully!');
            } else {
                alert('Error submitting configuration');
            }
        })
        .catch(error => console.error('Error:', error));
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setConfig(prev => ({
            ...prev,
            [name]: parseInt(value)
        }));
    };

    return (
        <div>
            <h2>Ticket Configuration</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>
                        Total Number of Tickets:
                        <input
                            type="number"
                            name="totalTickets"
                            value={config.totalTickets}
                            onChange={handleChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Ticket Release Rate:
                        <input
                            type="number"
                            name="releaseRate"
                            value={config.releaseRate}
                            onChange={handleChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Customer Retrieval Rate:
                        <input
                            type="number"
                            name="retrievalRate"
                            value={config.retrievalRate}
                            onChange={handleChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Maximum Ticket Capacity:
                        <input
                            type="number"
                            name="maxCapacity"
                            value={config.maxCapacity}
                            onChange={handleChange}
                        />
                    </label>
                </div>
                <button type="submit">Submit Configuration</button>
            </form>
        </div>
    );
}

export default TicketConfig; 